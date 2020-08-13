package com.beamtech.wordgame.service;

import com.beamtech.wordgame.model.GenericResponse;
import com.beamtech.wordgame.model.User;
import com.beamtech.wordgame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    public User login(String username, String password) {
        User dbUser = userRepository.findByUsername(username);
        if (dbUser != null && dbUser.getPassword().equals(password)) {
            messagingTemplate.convertAndSend("/topic/online-players", new GenericResponse()
                    .setCode(100));
            return dbUser;
        } else {
            return null;
        }
    }
}
