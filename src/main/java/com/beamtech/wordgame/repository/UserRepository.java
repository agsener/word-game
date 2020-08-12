package com.beamtech.wordgame.repository;

import com.beamtech.wordgame.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);
}
