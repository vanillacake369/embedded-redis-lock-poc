package com.example.embeddedredislockpoc.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisHashesOperatorTest {

    @Autowired
    private RedisHashesOperator redisHashesOperator;

    @Test
    @DisplayName("test should be")
    void testShouldBe() {
        // GIVEN
        String hashKey = "bike:1";
        String fieldKey = "model";

        // WHEN
        redisHashesOperator.find(hashKey, fieldKey);

        // THEN
//        fail("Not implemented");
    }

}