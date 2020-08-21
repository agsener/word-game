package com.beamtech.wordgame.service;

import com.beamtech.wordgame.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private Set<User> loggedUsers = new HashSet<>();

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    public User login(User user) {
        boolean isExist = loggedUsers.contains(user);
        if (!isExist) {
            user.setId(UUID.randomUUID().toString());
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

    @Scheduled(fixedRate = 10000L)
    public void timeout(){
        Date now = new Date();
        loggedUsers.forEach(user -> {
            if(now.getTime() - user.getLastBeat().getTime() > 6000){
                loggedUsers.remove(user);
            }
        });
    }

    public void beat(User user) {
        loggedUsers.forEach(u -> {
            if(u.getId().equals(user.getId())){
                u.setLastBeat(new Date());
            }
        });
    }
}
