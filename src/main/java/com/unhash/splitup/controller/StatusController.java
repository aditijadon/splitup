package com.unhash.splitup.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class StatusController {

    //METHOD TO CHECK THE STATUS OF SERVER

    @GetMapping
    @RequestMapping("/")
    public String statusCheck() {
        log.info("status controller is up!");
        return "Welcome to SplitUp!";
    }

}
