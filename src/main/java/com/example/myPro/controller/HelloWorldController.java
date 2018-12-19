package com.example.myPro.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @RequestMapping("/hello")
    public String index() {
        String test = "Hello World11122";
        return test;
    }
}
