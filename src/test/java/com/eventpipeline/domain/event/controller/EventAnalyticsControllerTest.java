package com.eventpipeline.domain.event.controller;

import com.eventpipeline.domain.event.dto.response.AnalyticsSummaryResponse;
import com.eventpipeline.domain.event.service.EventAnalyticsService;
import com.eventpipeline.domain.global.common.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventAnalyticsControllerTest {

    @Mock
    private EventAnalyticsService eventAnalyticsService;

    @InjectMocks
    private EventAnalyticsController eventAnalyticsController;

    @Test
    @DisplayName("getAnalytics() 호출 시 isSuccess=true와 집계 결과를 반환한다")
    void getAnalytics_returnsSuccessResponse() {
        AnalyticsSummaryResponse summary = new AnalyticsSummaryResponse(
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                0.0,
                Collections.emptyList()
        );
        when(eventAnalyticsService.getAnalyticsSummary()).thenReturn(summary);

        ApiResponse<AnalyticsSummaryResponse> response = eventAnalyticsController.getAnalytics();
        assertThat(response.getResult()).isEqualTo(summary);
    }
}
