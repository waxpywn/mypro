package com.example.myPro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyHtmlController {
    @RequestMapping("/myhtml")
    public String index() {
        return "index";
    }
}
