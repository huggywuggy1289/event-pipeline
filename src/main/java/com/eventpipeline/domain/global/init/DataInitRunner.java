package com.eventpipeline.domain.global.init;

import com.eventpipeline.domain.event.repository.projection.EventTypeCount;
import com.eventpipeline.domain.event.repository.projection.HourlyEventCount;
import com.eventpipeline.domain.event.repository.projection.UserEventCount;
import com.eventpipeline.domain.event.service.EventAnalyticsService;
import com.eventpipeline.domain.event.service.EventGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitRunner implements CommandLineRunner {

    private final EventGeneratorService eventGeneratorService;
    private final EventAnalyticsService eventAnalyticsService;

    @Override
    public void run(String... args) {
        eventGeneratorService.generateEvents(100);
        log.info("100개의 이벤트 데이터가 생성되었습니다.");

        log.info("========== 이벤트 분석 결과 ==========");

        log.info("[이벤트 타입별 발생 횟수]");
        for (EventTypeCount e : eventAnalyticsService.countByEventType()) {
            log.info("  {} : {}건", e.getEventType(), e.getCount());
        }

        log.info("[유저별 총 이벤트 수]");
        for (UserEventCount e : eventAnalyticsService.countByUserId()) {
            log.info("  {} : {}건", e.getUserId(), e.getCount());
        }

        log.info("[시간대별 이벤트 추이]");
        for (HourlyEventCount e : eventAnalyticsService.countByHour()) {
            log.info("  {}시 : {}건", e.getHour(), e.getCount());
        }

        log.info("[에러 이벤트 비율]");
        log.info("  {}", String.format("%.2f%%", eventAnalyticsService.errorEventRatio() * 100));

        log.info("======================================");
    }
}
