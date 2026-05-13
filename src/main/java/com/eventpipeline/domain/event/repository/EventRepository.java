package com.eventpipeline.domain.event.repository;

import com.eventpipeline.domain.event.entity.Event;
import com.eventpipeline.domain.event.entity.enums.EventType;
import com.eventpipeline.domain.event.repository.projection.ErrorMessageCount;
import com.eventpipeline.domain.event.repository.projection.EventTypeCount;
import com.eventpipeline.domain.event.repository.projection.HourlyEventCount;
import com.eventpipeline.domain.event.repository.projection.UserEventCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    /*
    이벤트 타입별 발생 횟수
     */
    @Query("SELECT e.eventType AS eventType, COUNT(e) AS count FROM Event e GROUP BY e.eventType")
    List<EventTypeCount> countByEventType();

    /*
    유저별 총 이벤트 수
     */
    @Query("SELECT e.userId, COUNT(e) FROM Event e GROUP BY e.userId")
    List<UserEventCount> countByUserId();

    /*
    시간대별 이벤트 추이
     */
    @Query("SELECT HOUR(e.createdAt), COUNT(e) FROM Event e GROUP BY HOUR(e.createdAt) ORDER BY HOUR(e.createdAt)")
    List<HourlyEventCount> countByHour();

    /*
    에러 이벤트 비율
     */
    long countAllByEventType(EventType eventType);

    /*
    에러 메시지별 발생 빈도
     */
    @Query("SELECT e.errorMessage, COUNT(e) FROM Event e WHERE e.eventType = 'ERROR' GROUP BY e.errorMessage")
    List<ErrorMessageCount> countByErrorMessage();
}
