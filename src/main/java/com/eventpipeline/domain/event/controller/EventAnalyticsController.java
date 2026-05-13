package com.eventpipeline.domain.event.controller;

import com.eventpipeline.domain.event.dto.response.AnalyticsSummaryResponse;
import com.eventpipeline.domain.event.service.EventAnalyticsService;
import com.eventpipeline.domain.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventAnalyticsController {

    private final EventAnalyticsService eventAnalyticsService;

    @GetMapping("/analytics")
    public ApiResponse<AnalyticsSummaryResponse> getAnalytics() {
        return ApiResponse.onSuccess(eventAnalyticsService.getAnalyticsSummary());
    }
}
