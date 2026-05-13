package com.eventpipeline.domain.event.repository.projection;

public interface HourlyEventCount {
    Integer getHour();
    Long getCount();
}
