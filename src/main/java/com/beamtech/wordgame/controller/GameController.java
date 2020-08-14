package com.beamtech.wordgame.controller;

import com.beamtech.wordgame.model.GenericResponse;
import com.beamtech.wordgame.model.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.beamtech.wordgame.controller.UserController.LOGGEDIN_USER;

@RestController
@RequestMapping("game")
public class GameController {

    @PostMapping("startGame")
    public GenericResponse startGame(HttpSession session, User user){
        User current = (User) session.getAttribute(LOGGEDIN_USER);
        if(user != null){

            return new GenericResponse()
                    .setCode(0);
        }else{
            return new GenericResponse()
                    .setCode(10);
        }
    }
}