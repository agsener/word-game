package com.beamtech.wordgame.controller;

import com.beamtech.wordgame.dto.GameDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("game")
public class GameController {

    public List<GameDto> games = new ArrayList<GameDto>();


    @GetMapping("active-games")
    public List<GameDto> activeGames(){
        return games;
    }

}