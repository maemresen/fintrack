package com.maemresen.fintrack.business.it;

import com.maemresen.fintrack.business.service.HelloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloServiceIT extends AbstractBaseBusinessIT {

    @Autowired
    private HelloService helloService;

    @Test
    void helloServiceTest() {
        int sum = assertDoesNotThrow(() -> helloService.sum(5, 2));
        assertEquals(7, sum);
    }
}
