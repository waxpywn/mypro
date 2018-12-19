package com.example.myPro.model.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TestProperties {

    @Value("${com.test.properties.value}")
    private String testValue;

    public String getTestValue() {
        return testValue;
    }

    public void setTestValue(String testValue) {
        this.testValue = testValue;
    }
}
