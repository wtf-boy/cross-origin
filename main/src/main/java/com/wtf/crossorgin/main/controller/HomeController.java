package com.wtf.crossorgin.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wtf
 * @date 2023/8/17 10:44
 */
@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String index() {
        return "index";
    }
}
