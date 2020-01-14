package com.payline.payment.alipay.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TransactionManagerServiceImplTest {

    @InjectMocks
    private TransactionManagerServiceImpl service;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void readAdditionalData_notNull(){
        // when: calling the readAdditionalData method with any string
        Map<String, String> result = service.readAdditionalData("any", "any");

        // then: the resulting map must not be null
        assertNotNull( result );
    }

}
