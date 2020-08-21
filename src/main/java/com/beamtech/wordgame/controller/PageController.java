package com.beamtech.wordgame.controller;

import com.beamtech.wordgame.model.User;
import com.beamtech.wordgame.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

import static com.beamtech.wordgame.controller.UserController.LOGGEDIN_USER;

@Controller
public class PageController {

    private Logger logger = LoggerFactory.getLogger(PageController.class);

    @Autowired
    private UserService userService;

    @GetMapping({"/", "/lobby", "/game/{id}"})
    public String index(){
        return "index";
    }

    @MessageMapping("/beat")
    public void greeting(User user) throws Exception {
        userService.beat(user);
    }
}
