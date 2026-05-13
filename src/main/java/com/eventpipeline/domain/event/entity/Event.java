package com.eventpipeline.domain.event.entity;

import com.eventpipeline.domain.event.entity.enums.EventType;
import com.eventpipeline.domain.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private String userId;
    private String sessionId;
    private String screenName;
    private BigDecimal amount;
    private String errorMessage;
}
