package com.eventpipeline.domain.event.service;

import com.eventpipeline.domain.event.entity.Event;
import com.eventpipeline.domain.event.repository.EventRepository;
import com.eventpipeline.domain.event.repository.projection.ErrorMessageCount;
import com.eventpipeline.domain.event.repository.projection.EventTypeCount;
import com.eventpipeline.domain.event.repository.projection.HourlyEventCount;
import com.eventpipeline.domain.event.repository.projection.UserEventCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventAnalyticsService {

    private final EventRepository eventRepository;

    // 이벤트 타입별 발생 횟수 그대로 반환
    @Transactional(readOnly = true)
    public List<EventTypeCount> countByEventType(){
        return eventRepository.countByEventType();
    }

    // 유저별 총 이벤트 수 그대로 반환
    @Transactional(readOnly = true)
    public List<UserEventCount> countByUserId() {
        return eventRepository.countByUserId();
    }

    // 시간대별 이벤트 추이 그대로 반환
    @Transactional(readOnly = true)
    public List<HourlyEventCount> countByHour() {
        return eventRepository.countByHour();
    }

    // 에러 이벤트 비율 그대로 반환
    @Transactional(readOnly = true)
    public Double errorEventRatio() {
        return eventRepository.errorEventRatio();
    }

    // 에러 메시지별 발생 빈도 그대로 반환
    @Transactional(readOnly = true)
    public List<ErrorMessageCount> countByErrorMessage() {
        return eventRepository.countByErrorMessage();
    }
}
