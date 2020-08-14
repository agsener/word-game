package com.beamtech.wordgame.controller;

import com.beamtech.wordgame.dto.GameDto;
import com.beamtech.wordgame.model.GenericResponse;
import com.beamtech.wordgame.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.beamtech.wordgame.controller.UserController.LOGGEDIN_USER;

@RestController
@RequestMapping("game")
public class GameController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @PostMapping("game-request")
    public GenericResponse gameRequest(@RequestBody User receiver, HttpSession session) {
        User sender = (User) session.getAttribute(LOGGEDIN_USER);
        GameDto gameDto = new GameDto();
        gameDto.setSender(sender);
        gameDto.setReceiver(receiver);
        messagingTemplate.convertAndSend("/topic/game-request", gameDto);
        return new GenericResponse()
                .setCode(0);
    }

    @PostMapping("startGame")
    public GenericResponse startGame(HttpSession session, User user) {
        User current = (User) session.getAttribute(LOGGEDIN_USER);
        if (user != null) {

            return new GenericResponse()
                    .setCode(0);
        } else {
            return new GenericResponse()
                    .setCode(10);
        }
    }
}