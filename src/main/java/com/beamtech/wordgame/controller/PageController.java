package com.beamtech.wordgame.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", "/lobby"})
    public String index(){
        return "index";
    }
}
