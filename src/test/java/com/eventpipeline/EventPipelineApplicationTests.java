package com.eventpipeline;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Docker 환경에서만 실행 가능 (로컬 DB 미사용)")
class EventPipelineApplicationTests {

    @Test
    void contextLoads() {
    }

}
