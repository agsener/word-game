package com.beamtech.wordgame.service;

import com.beamtech.wordgame.dto.GameDto;
import com.beamtech.wordgame.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GameService {

    private List<GameDto> activeGames = new ArrayList<>();

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    public GameDto createNewGame(User player1, User player2) {
        GameDto dto = new GameDto()
                .setSender(player1)
                .setReceiver(player2)
                .setId(UUID.randomUUID().toString());

        activeGames.add(dto);

        return dto;
    }


    @Scheduled(fixedRate = 5000L)
    public void broadcastLiveUsers() {
        messagingTemplate.convertAndSend("/topic/active-games", activeGames);
    }

}
