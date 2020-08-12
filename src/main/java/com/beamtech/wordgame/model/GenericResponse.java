package com.beamtech.wordgame.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GenericResponse<T> {

    private int code;
    private String message;
    private T data;
}
