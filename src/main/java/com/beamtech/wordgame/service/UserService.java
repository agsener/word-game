package com.beamtech.wordgame.service;

import com.beamtech.wordgame.model.User;
import com.beamtech.wordgame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private Set<User> loggedUsers = new HashSet<>();

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

    @Scheduled(fixedRate = 2000L)
    public void broadcastLiveUsers() {
        messagingTemplate.convertAndSend("/topic/online-players", loggedUsers);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
