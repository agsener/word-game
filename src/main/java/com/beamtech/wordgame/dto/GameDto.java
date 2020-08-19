package com.beamtech.wordgame.dto;

import com.beamtech.wordgame.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class GameDto {

    private String id;

    private User sender;

    private User receiver;

    @JsonIgnore
    private String word;

    public GameDto setWord(String word) {
        this.word = word;
        for (char letter : word.toCharArray()) {
            LetterDto letterDto = new LetterDto()
                    .setChosen(false);
            letters.add(letterDto);
        }
        return this;
    }

    private List<LetterDto> letters = new ArrayList<>();

    // turn 0 ise sira sender'da, turn 1 ise sira receiver'da
    String whosTurn;
}
