package com.beamtech.wordgame.service;

import com.beamtech.wordgame.dto.GameDto;
import com.beamtech.wordgame.dto.LetterDto;
import com.beamtech.wordgame.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
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

    public GameDto takeWord(User user) {
        List<String> words = new ArrayList<>();
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
        String randomWord = words.get((int) (Math.random() * ((words.size()))));

        GameDto currentGameDto = new GameDto();
        currentGameDto.setTurn((int) (Math.random() * 1));
        for (GameDto gameDto : activeGames) {
            if (gameDto.getSender().equals(user) || gameDto.getReceiver().equals(user)) {
                currentGameDto = gameDto;
                break;
            }
        }

        for (char letter : randomWord.toCharArray()) {
            LetterDto letterDto = new LetterDto()
                    .setName(letter + "")
                    .setChosen(false);
            currentGameDto.getLetters().add(letterDto);
        }
        return currentGameDto;
    }


    @Scheduled(fixedRate = 5000L)
    public void broadcastLiveUsers() {
        messagingTemplate.convertAndSend("/topic/active-games", activeGames);
    }

}
