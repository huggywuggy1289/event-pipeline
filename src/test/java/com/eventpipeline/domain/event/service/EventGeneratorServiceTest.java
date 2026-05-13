package com.eventpipeline.domain.event.service;

import com.eventpipeline.domain.event.entity.Event;
import com.eventpipeline.domain.event.entity.enums.EventType;
import com.eventpipeline.domain.event.repository.EventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventGeneratorServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventGeneratorService eventGeneratorService;

    @Captor
    private ArgumentCaptor<List<Event>> eventsCaptor;

    @Test
    @DisplayName("generateEvents(100) 호출 시 100건이 저장된다")
    void generateEvents_savesExactCount() {
        eventGeneratorService.generateEvents(100);

        verify(eventRepository).saveAll(eventsCaptor.capture());
        assertThat(eventsCaptor.getValue()).hasSize(100);
    }

    @Test
    @DisplayName("EXPENSE_CREATED 이벤트는 amount가 null이 아니다")
    void expenseCreatedEvent_hasAmount() {
        eventGeneratorService.generateEvents(500);

        verify(eventRepository).saveAll(eventsCaptor.capture());
        List<Event> expenseEvents = eventsCaptor.getValue().stream()
                .filter(e -> e.getEventType() == EventType.EXPENSE_CREATED)
                .toList();

        assertThat(expenseEvents).isNotEmpty();
        assertThat(expenseEvents).allSatisfy(event ->
                assertThat(event.getAmount()).isNotNull()
        );
    }

    @Test
    @DisplayName("ERROR 이벤트는 errorMessage가 null이 아니다")
    void errorEvent_hasErrorMessage() {
        eventGeneratorService.generateEvents(500);

        verify(eventRepository).saveAll(eventsCaptor.capture());
        List<Event> errorEvents = eventsCaptor.getValue().stream()
                .filter(e -> e.getEventType() == EventType.ERROR)
                .toList();

        assertThat(errorEvents).isNotEmpty();
        assertThat(errorEvents).allSatisfy(event ->
                assertThat(event.getErrorMessage()).isNotNull()
        );
    }
}
