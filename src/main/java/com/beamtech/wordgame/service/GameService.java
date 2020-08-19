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
        words.add("Authentication");
        words.add("Array");
        words.add("Object");
        words.add("Sublime");
        words.add("Github");
        words.add("Database");
        words.add("Heroku");
        words.add("Terminal");
        words.add("Inheritance");
    }

    private Map<String, GameDto> activeGames = new HashMap<>();

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    public GameDto createNewGame(User player1, User player2) {
        int randomTurn = (int) (Math.random() * 1);
        GameDto dto = new GameDto()
                .setSender(player1)
                .setReceiver(player2)
                .setWord(getRandomWord())
                .setWhosTurn(randomTurn == 0 ? player1.getUsername() : player2.getUsername())
                .setId(UUID.randomUUID().toString());

        activeGames.put(dto.getId(), dto);

        return dto;
    }

    public String getRandomWord() {
        return words.get((int) (Math.random() * ((words.size()))));
    }

    public GameDto getGame(String id) {
        return activeGames.get(id);
    }

    //Oyuncu ui'dan harf tahmini yaptiginda calisan fonksiyon
    public GameDto move(String game, char letter, String currentTurn) {
        GameDto gameDto = activeGames.get(game);

        // Harf gizli kelimenin icinde varsa if'e giriyor
        if (gameDto.getWord().toLowerCase().contains(letter + "")) {
            char[] charArray = gameDto.getWord().toLowerCase().toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                if (c == letter) {
                    gameDto.getLetters().get(i).setChosen(true);
                    gameDto.getLetters().get(i).setName(letter + "");
                }
            }
        }
        // Harf tahmini yanlis ise turn diger oyuncuya geciyor.
        else {
            if (currentTurn.equals(gameDto.getSender().getUsername())) {
                gameDto.setWhosTurn(gameDto.getReceiver().getUsername());
            } else {
                gameDto.setWhosTurn(gameDto.getSender().getUsername());
            }
        }
        gameState(gameDto);
        return gameDto;
    }

    public void gameState(GameDto gameDto) {
        messagingTemplate.convertAndSend("/topic/game/" + gameDto.getId(), gameDto);
    }

    @Scheduled(fixedRate = 5000L)
    public void broadcastLiveUsers() {
        messagingTemplate.convertAndSend("/topic/active-games", activeGames.values());
    }
}
