package com.beamtech.wordgame;


import com.beamtech.wordgame.model.User;
import com.beamtech.wordgame.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class  UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void createUser() {
        User user = new User()
                .setUsername("player3")
                .setPassword("asd");
        user.setId(UUID.randomUUID().toString());

        userRepository.save(user);
    }

    @Test
    public void deneme() {
        for (int i = 0; i < 100; i++) {
            int a = (int) (Math.random() * 2);
            System.out.println("random number: " + a);
        }
    }
}
