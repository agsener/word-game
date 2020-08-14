package com.beamtech.wordgame.dto;

import com.beamtech.wordgame.model.User;
import lombok.Data;

@Data
public class GameDto {

    private User sender;

    private User receiver;
}
