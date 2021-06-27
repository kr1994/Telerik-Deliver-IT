package com.telerikacademy.web.deliver_it.controllers.mvc;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logged")
public class HomeMvcController {

    @GetMapping
    public String showHomePage() {
        return "index";
    }
}

