package com.liftlab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
public class Controller {

    @GetMapping("/hello")
    public String getData() {
        return "Hello from Server 1";
    }
}
