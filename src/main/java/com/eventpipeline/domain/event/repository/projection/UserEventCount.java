package com.eventpipeline.domain.event.repository.projection;

public interface UserEventCount {
    String getUserId();
    Long getCount();
}