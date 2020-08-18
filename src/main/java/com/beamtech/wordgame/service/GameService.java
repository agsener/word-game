package com.beamtech.wordgame.service;

import com.beamtech.wordgame.dto.GameDto;
import com.beamtech.wordgame.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class GameService {

    List<String> words = new ArrayList<>();


    @PostConstruct
    public void init() {
        words.add("AngularJS");
        words.add("Bootstrap");
        words.add("JavaScript");
        words.add("authentication");
        words.add("array");
        words.add("object");
        words.add("sublime");
        words.add("github");
        words.add("database");
        words.add("Heroku");
        words.add("terminal");
        words.add("inheritance");
    }


    private Map<String, GameDto> activeGames = new HashMap<>();

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    public GameDto createNewGame(User player1, User player2) {
        GameDto dto = new GameDto()
                .setSender(player1)
                .setReceiver(player2)
                .setWord(getRandomWord())
                .setTurn((int) (Math.random() * 1))
                .setId(UUID.randomUUID().toString());

        activeGames.put(dto.getId(), dto);

        return dto;
    }

    public String getRandomWord() {
        return words.get((int) (Math.random() * ((words.size()))));
    }

    @Scheduled(fixedRate = 5000L)
    public void broadcastLiveUsers() {
        messagingTemplate.convertAndSend("/topic/active-games", activeGames.values());
    }

    public GameDto getGame(String id) {
        return activeGames.get(id);
    }

    public GameDto move(String game, char letter) {
        GameDto gameDto = activeGames.get(game);

        if (gameDto.getWord().contains(letter + "")) {
            char[] charArray = gameDto.getWord().toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                if (c == letter) {
                    gameDto.getLetters().get(i).setChosen(true);
                    gameDto.getLetters().get(i).setName(letter + "");
                }
            }
        } else {
            // move turn
        }

        return gameDto;
    }
}
