# Event Pipeline

## 실행 방법

```bash
# 1. Docker 컨테이너 실행 (MySQL + Spring Boot 앱)
docker-compose up -d

# 2. 앱이 완전히 올라올 때까지 대기 후 API 확인
curl http://localhost:8080/api/events/analytics

# 3. 시각화 (Python 3.0 이상)
pip install matplotlib requests
python visualize.py
# → charts/analytics_summary.png가 생성됩니다.
```

- 앱 시작 시 `DataInitRunner`가 157개의 샘플 이벤트를 자동 생성하고, 집계 결과를 콘솔에 출력합니다.
- 종료: `docker-compose down`

## 스키마 설명

### events 테이블

| 컬럼 | 타입 | 설명 |
|---|---|---|
| `id` | BIGINT (PK, AUTO_INCREMENT) | 기본키 |
| `event_type` | VARCHAR (ENUM) | PAGE_VIEW, EXPENSE_CREATED, EXPENSE_REVIEWED, ERROR |
| `user_id` | VARCHAR | 유저 식별자 |
| `session_id` | VARCHAR | 세션 UUID |
| `screen_name` | VARCHAR | 화면 이름 |
| `amount` | DECIMAL | 지출 금액 (EXPENSE_CREATED만 사용) |
| `error_message` | VARCHAR | 에러 메시지 (ERROR만 사용) |
| `created_at` | DATETIME | 생성 시각 |

### 이렇게 설계한 이유

모든 이벤트 타입을 하나의 테이블에 저장하는 **단일 테이블 전략**을 선택했습니다.
이벤트 타입별로 테이블을 분리하면 타입 간 집계(전체 이벤트 수, 에러 비율 등)에 JOIN이 필요해지고 스키마가 복잡해집니다.
단일 테이블로 두면 `GROUP BY event_type` 하나로 모든 집계를 처리할 수 있고, `amount`와 `error_message`는 해당 타입에서만 값이 들어가므로 nullable로 두어 공간 낭비를 최소화했습니다.

## 구현하면서 맞닥드린 문제 상황과 고민한 점

- **JPQL projection 매핑 문제**: Spring Data JPA에서 인터페이스 기반 projection을 사용할 때, `SELECT` 절의 alias와 인터페이스의 getter 이름이 정확히 일치하지 않으면 `null`이 반환되는 문제를 겪었습니다. 예를 들어 `COUNT(e) AS count`로 작성해야 `getCount()`와 매핑되는데, alias를 누락해서 디버깅에 시간을 썼습니다.
- **에러 비율 계산**: 처음에는 JPQL 서브쿼리로 에러 비율을 한 번에 계산하려 했으나, JPA에서 `FROM` 절 서브쿼리를 지원하지 않아 서비스 레이어에서 `countAllByEventType(ERROR) / count()`로 나누어 계산하는 방식으로 변경했습니다.
- **시각화 차트 선택**: 시간대별 추이 차트는 샘플 데이터가 동시에 생성되어 의미 있는 분포가 나오지 않으므로 제외하고, 실제 인사이트를 줄 수 있는 4개 차트(타입별 횟수, 유저별 횟수, 에러 비율, 에러 메시지 빈도)에 집중했습니다.
- **이벤트 개수 변경**: 기존 100건에서는 ERROR 16건 = 16.00%로 바로 읽혀서 비율 집계의 의미가 희석되었습니다. 157건으로 변경하면 직관적으로 환산이 어려워지므로 에러 비율 집계의 존재 이유가 명확해집니다.
- **MySQL 선택 이유**: 집계/필터링 쿼리를 SQL로 직접 수행하기 위해 RDBMS가 필요했고, PostgreSQL보다 사용 경험이 많아 빠르게 구성할 수 있다고 판단했습니다.

---

## Step 1. 이벤트 생성기

### 이벤트 타입 설계

제가 현재 운영중인 서비스에서 자주 발생하는 4가지 이벤트를 기반으로 설계했습니다.

| 이벤트 타입 | 설명       | 비고 |
|---|----------|---|
| `PAGE_VIEW` | 화면 조회    | 가장 빈번한 기본 행동 로그 |
| `EXPENSE_CREATED` | 지출 기록    | 핵심 비즈니스 액션, `amount` 필드 포함 |
| `EXPENSE_REVIEWED` | 소비 회고 작성 | 사용자 참여도를 측정할 수 있는 이벤트 |
| `ERROR` | 오류 발생    | 서비스 안정성 모니터링용, `errorMessage` 필드 포함 |

### 설계 의도

- **PAGE_VIEW**: 유저 트래픽과 화면별 방문 빈도를 분석하기 위한 기본 이벤트
- **EXPENSE_CREATED**: 현재 운영 중인 지출관리 서비스의 핵심 액션으로, 실제 전환율과 직결되는 이벤트(분석 단계에서 총 지출액, 평균 지출액 등을 산출가능)
- **EXPENSE_REVIEWED**: 단순 기록을 넘어 사용자가 소비를 돌아보는 행동을 추적
- **ERROR**: 서비스 장애와 오류 패턴을 파악하기 위한 이벤트

## Step 2. 로그 저장

- MySQL(Docker)에 JPA 엔티티로 필드를 컬럼별로 분리하여 저장
- 스키마 및 설계 이유는 상단 [스키마 설명](#스키마-설명) 참고