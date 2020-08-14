package com.beamtech.wordgame.dto;

import com.beamtech.wordgame.model.User;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GameDto {

    private String id;

    private User sender;

    private User receiver;
}
