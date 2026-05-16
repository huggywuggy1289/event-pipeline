package com.eventpipeline.domain.event.service;

import com.eventpipeline.domain.event.entity.Event;
import com.eventpipeline.domain.event.entity.enums.EventType;
import com.eventpipeline.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventGeneratorService {

    private final EventRepository eventRepository;
    private final Random random = new Random();

    private static final String[] USER_IDS = {"user_001", "user_002", "user_003", "user_004", "user_005"};
    private static final String[] SCREEN_NAMES = {"home", "expense_list", "report", "settings"};
    private static final String[] ERROR_MESSAGES = {"NullPointerException", "NetworkTimeoutException", "UnknownException"};

    // 이벤트 생성 -> 저장
    @Transactional
    public void generateEvents(int count){
        List<Event> events = new ArrayList<>();
        for(int i = 0; i < count; i++){
            events.add(createRandomEvent());
        }
        eventRepository.saveAll(events);
    }

    // 이벤트 랜덤 생성
    private Event createRandomEvent(){
        EventType eventType = EventType.values()[random.nextInt(EventType.values().length)];
        String userId = USER_IDS[random.nextInt(USER_IDS.length)];
        String sessionId = UUID.randomUUID().toString();
        String screenName = SCREEN_NAMES[random.nextInt(SCREEN_NAMES.length)];

        BigDecimal amount = null;
        String errorMessage = null;

        if(eventType == EventType.EXPENSE_CREATED){
            amount = BigDecimal.valueOf(random.nextInt(100000) + 1000);
        }else if (eventType == EventType.EXPENSE_REVIEWED) {
            screenName = "expense_review";
        }else if(eventType == EventType.ERROR){
            errorMessage = ERROR_MESSAGES[random.nextInt(ERROR_MESSAGES.length)];
        }
        return Event.builder()
                .eventType(eventType)
                .userId(userId)
                .sessionId(sessionId)
                .screenName(screenName)
                .amount(amount)
                .errorMessage(errorMessage)
                .build();
    }
}
