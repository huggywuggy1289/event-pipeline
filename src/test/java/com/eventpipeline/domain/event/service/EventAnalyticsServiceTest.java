package com.eventpipeline.domain.event.service;

import com.eventpipeline.domain.event.entity.enums.EventType;
import com.eventpipeline.domain.event.repository.EventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventAnalyticsServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventAnalyticsService eventAnalyticsService;

    @Nested
    @DisplayName("errorEventRatio()")
    class ErrorEventRatio {

        @Test
        @DisplayName("에러가 0건이면 0.0을 반환한다")
        void returnsZero_whenNoErrors() {
            when(eventRepository.countAllByEventType(EventType.ERROR)).thenReturn(0L);
            when(eventRepository.count()).thenReturn(100L);

            Double ratio = eventAnalyticsService.errorEventRatio();

            assertThat(ratio).isEqualTo(0.0);
        }

        @Test
        @DisplayName("에러 비율을 정확히 계산한다")
        void calculatesCorrectly() {
            when(eventRepository.countAllByEventType(EventType.ERROR)).thenReturn(15L);
            when(eventRepository.count()).thenReturn(100L);

            Double ratio = eventAnalyticsService.errorEventRatio();

            assertThat(ratio).isEqualTo(0.15);
        }
    }

}
