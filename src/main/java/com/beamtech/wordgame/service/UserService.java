package com.beamtech.wordgame.service;

import com.beamtech.wordgame.model.User;
import com.beamtech.wordgame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private List<User> loggedUsers = new ArrayList<>();

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    public User login(String username, String password) {
        User dbUser = userRepository.findByUsername(username);
        if (dbUser != null && dbUser.getPassword().equals(password)) {
            loggedUsers.add(dbUser);
            return dbUser;
        } else {
            return null;
        }
    }

    @Scheduled(fixedRate = 5000L)
    public void broadcastLiveUsers() {
        messagingTemplate.convertAndSend("/topic/online-players", loggedUsers);
    }
}
