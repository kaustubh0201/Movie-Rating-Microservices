package com.practice.springboot.springbootconfig.controller;

import com.practice.springboot.springbootconfig.configuration.DbSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GreetingController {

    @Value("${my.greetings}")
    private String greetingMessage;

    @Value("some static messages")
    private String staticMessages;

    @Value("${my.list.values}")
    private List<String> listValue;

    @Value("#{${dbValues}}")
    private Map<String, String> dbValues;

    @Autowired
    private DbSettings dbSettings;

    public GreetingController() {

    }

    @Value("${app.description}")
    private String appDescription;

    @GetMapping("/greeting")
    public String greeting() {
        return dbSettings.getConnection() + dbSettings.getHost();
    }
}
