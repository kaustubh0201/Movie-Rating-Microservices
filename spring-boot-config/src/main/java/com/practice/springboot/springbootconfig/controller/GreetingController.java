package com.practice.springboot.springbootconfig.controller;

import com.practice.springboot.springbootconfig.configuration.DbSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RefreshScope
public class GreetingController {

    @Value("${my.greeting}")
    private String greetingMessage;

    @Value("some static messages")
    private String staticMessages;

    @Value("${my.list.values}")
    private List<String> listValue;

    @Value("#{${dbValues}}")
    private Map<String, String> dbValues;

    @Autowired
    private DbSettings dbSettings;

    @Autowired
    private Environment env;

    public GreetingController() {

    }

    @Value("${app.description}")
    private String appDescription;

    @GetMapping("/connection-details")
    public String connectionDetails() {
        return dbSettings.getConnection() + dbSettings.getHost() + dbSettings.getPort();
    }

    @GetMapping("/greeting")
    public String greetings() {
        return greetingMessage;
    }

    @GetMapping("/envdetails")
    public String envDetails() {
        return env.toString();
    }
}
