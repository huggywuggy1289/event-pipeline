package com.eventpipeline.domain.global.init;

import com.eventpipeline.domain.event.service.EventGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitRunner implements CommandLineRunner {

    private final EventGeneratorService eventGeneratorService;

    @Override
    public void run(String... args) {
        eventGeneratorService.generateEvents(100);
        log.info("100개의 이벤트 데이터가 생성되었습니다.");
    }
}
