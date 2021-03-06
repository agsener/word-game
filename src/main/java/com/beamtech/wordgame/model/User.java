package com.beamtech.wordgame.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "user")
@TypeAlias("user")
@Accessors(chain = true)
@Data
public class User extends Base {

    private String username;

    private Date lastBeat;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return getUsername().equals(((User) obj).getUsername());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
