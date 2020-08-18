package com.beamtech.wordgame.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LetterDto {

    String name;

    boolean chosen;
}
