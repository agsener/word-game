package com.beamtech.wordgame.service;

import com.beamtech.wordgame.dto.GameDto;
import com.beamtech.wordgame.dto.LetterDto;
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
    String[] a = "abcçdefgğhıijklmnoöpqrsştuüvyz".split("(?!^)");

    @PostConstruct
    public void init() {
        words.add("Kabil");
        words.add("Viyana");
        words.add("Bakü");
        words.add("Brüksel");
        words.add("Saraybosna");
        words.add("Pekin");
        words.add("Lefkoşa");
        words.add("Prag");
        words.add("Paris");
        words.add("Berlin");
        words.add("Bağdat");
        words.add("Roma");
        words.add("Amsterdam");
        words.add("Lizbon");
        words.add("Moskova");
        words.add("Singapur");
        words.add("Madrid");
        words.add("Şam");
        words.add("Tunus");
        words.add("Cezayir");
        words.add("Sofya");
        words.add("Ottawa");
        words.add("Havana");
        words.add("Kopenhag");
        words.add("Kahire");
        words.add("Helsinki");
        words.add("Budapeşte");
        words.add("Tahran");
        words.add("Tokyo");
        words.add("Seul");
        words.add("Varşova");
        words.add("Stokholm");

    }

    private Map<String, GameDto> activeGames = new HashMap<>();

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    public GameDto createNewGame(User player1, User player2) {
        int randomTurn = (int) (Math.random() * 2);
        String randomWord = getRandomWord();
        GameDto dto = new GameDto()
                .setSender(player1)
                .setReceiver(player2)
                .setWord(randomWord)
                .setRemainingLetters(randomWord.length())
                .setWhosTurn(randomTurn == 0 ? player1.getUsername() : player2.getUsername())
                .setAlphabet(a)
                .setId(UUID.randomUUID().toString());
        activeGames.put(dto.getId(), dto);
        return dto;
    }

    public String getRandomWord() {
        return words.get((int) (Math.random() * ((words.size())))).toLowerCase();
    }

    public GameDto getGame(String id) {
        return activeGames.get(id);
    }

    //Oyuncu ui'dan harf tahmini yaptiginda calisan fonksiyon
    public GameDto move(String game, char letter, String currentTurn) {
        GameDto gameDto = activeGames.get(game);
        // Harf gizli kelimenin icinde varsa if'e giriyor
        if (gameDto.getWord().contains(letter + "")) {
            char[] charArray = gameDto.getWord().toCharArray();
            int numberOfInstance = 0;
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                if (c == letter) {
                    gameDto.getLetters().get(i).setChosen(true);
                    gameDto.getLetters().get(i).setName(letter + "");
                    numberOfInstance++;
                }
            }
            gameDto.setRemainingLetters(gameDto.getRemainingLetters() - numberOfInstance);
            checkForEndOfGame(gameDto, currentTurn);
        }
        // Harf tahmini yanlis ise turn diger oyuncuya geciyor.
        else {
            if (currentTurn.equals(gameDto.getSender().getUsername())) {
                gameDto.setWhosTurn(gameDto.getReceiver().getUsername());
            } else {
                gameDto.setWhosTurn(gameDto.getSender().getUsername());
            }
        }
        for (LetterDto l : gameDto.getAlphabet()) {
            if (letter == l.getName().charAt(0)) {
                l.setChosen(true);
            }
        }
        gameState(gameDto);
        return gameDto;
    }

    public void gameState(GameDto gameDto) {
        messagingTemplate.convertAndSend("/topic/game/" + gameDto.getId(), gameDto);
    }

    private void checkForEndOfGame(GameDto gameDto, String currentTurn) {
        if (gameDto.getRemainingLetters() == 0) {
            gameDto.setWinner(currentTurn);
            activeGames.remove(gameDto.getId(), gameDto);
        }
    }

    @Scheduled(fixedRate = 2000L)
    public void broadcastLiveUsers() {
        messagingTemplate.convertAndSend("/topic/active-games", activeGames.values());
    }
}
