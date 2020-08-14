package com.beamtech.wordgame.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
@TypeAlias("user")
@Accessors(chain = true)
@Data
public class User extends Base {

    private String username;

    private String password;
}
