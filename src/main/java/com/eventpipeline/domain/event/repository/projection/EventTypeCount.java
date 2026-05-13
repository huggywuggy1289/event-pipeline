package com.eventpipeline.domain.event.repository.projection;

public interface EventTypeCount {
    String getEventType();
    Long getCount();
}
