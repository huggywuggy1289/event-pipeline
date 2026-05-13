package com.eventpipeline.domain.event.dto.response;

import com.eventpipeline.domain.event.repository.projection.ErrorMessageCount;
import com.eventpipeline.domain.event.repository.projection.EventTypeCount;
import com.eventpipeline.domain.event.repository.projection.HourlyEventCount;
import com.eventpipeline.domain.event.repository.projection.UserEventCount;

import java.util.List;

public record AnalyticsSummaryResponse(
        List<EventTypeCount> eventTypeCounts,
        List<UserEventCount> userEventCounts,
        List<HourlyEventCount> hourlyEventCounts,
        Double errorEventRatio,
        List<ErrorMessageCount> errorMessageCounts
) {
}
