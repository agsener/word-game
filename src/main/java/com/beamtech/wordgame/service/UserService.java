package com.beamtech.wordgame.service;

import com.beamtech.wordgame.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
public class UserService {

    private Set<User> loggedUsers = new HashSet<>();

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    public User login(User user) {
        boolean isExist = loggedUsers.contains(user);
        if (!isExist) {
            loggedUsers.add(user);
            return user;
        } else {
            return null;
        }
    }

    @Scheduled(fixedRate = 2000L)
    public void broadcastLiveUsers() {
        messagingTemplate.convertAndSend("/topic/online-players", loggedUsers);
    }

    public User findByUsername(String username) {
        Iterator<User> userIterator = loggedUsers.iterator();
        User user;
        while (userIterator.hasNext()) {
            user = userIterator.next();
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
