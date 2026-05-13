package com.eventpipeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EventPipelineApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventPipelineApplication.class, args);
    }

}
