package com.eventpipeline.domain.event.repository;

import com.eventpipeline.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
