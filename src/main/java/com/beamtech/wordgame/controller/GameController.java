package com.beamtech.wordgame.controller;

import com.beamtech.wordgame.dto.GameDto;
import com.beamtech.wordgame.model.GenericResponse;
import com.beamtech.wordgame.model.User;
import com.beamtech.wordgame.service.GameService;
import com.beamtech.wordgame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static com.beamtech.wordgame.controller.UserController.LOGGEDIN_USER;

@RestController
@RequestMapping("game")
public class GameController {

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @PostMapping("game-request")
    public GenericResponse gameRequest(@RequestParam("username") String username, HttpSession session) {
        User sender = (User) session.getAttribute(LOGGEDIN_USER);
        messagingTemplate.convertAndSend("/topic/game-request/" + username, new GenericResponse()
                .setCode(0)
                .setData(sender));
        return new GenericResponse()
                .setCode(0);
    }

    @PostMapping("reject")
    public void reject(@RequestParam("username") String username, HttpSession session) {
        User sender = (User) session.getAttribute(LOGGEDIN_USER);
        messagingTemplate.convertAndSend("/topic/game-request/" + username, new GenericResponse()
                .setCode(100)
                .setData(sender));
    }

    @PostMapping("accept")
    public GameDto accept(@RequestParam("username") String username, HttpSession session) {
        User sender = (User) session.getAttribute(LOGGEDIN_USER);

        User receiver = userService.findByUsername(username);

        GameDto game = gameService.createNewGame(sender, receiver);

        messagingTemplate.convertAndSend("/topic/game-request/" + username, new GenericResponse()
                .setCode(200)
                .setData(game));

        return game;
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

    @GetMapping("game")
    public GameDto takeWord(@RequestParam("id") String id) {
        return gameService.getGame(id);
    }

    @PostMapping("select")
    public GameDto guess(@RequestParam("id") String game,
                         @RequestParam("letter") char letter,
                         @RequestParam("turn") String currentTurn) {
        return gameService.move(game, letter, currentTurn);
    }


}