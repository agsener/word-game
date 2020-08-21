package com.beamtech.wordgame.controller;

import com.beamtech.wordgame.model.GenericResponse;
import com.beamtech.wordgame.model.User;
import com.beamtech.wordgame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("user")
public class UserController {

    public static final String LOGGEDIN_USER = "LOGGEDIN_USER";

    @Autowired
    private UserService userService;

    /**
     * Return:
     * 0 -> success
     * 10 -> Cannot find user with @user.username
     */
    @PostMapping("login")
    public GenericResponse login(@RequestBody User user, HttpSession session) {
        User logedUser = userService.login(user);
        if (logedUser != null) {
            session.setAttribute(LOGGEDIN_USER, logedUser);
            return new GenericResponse()
                    .setCode(0)
                    .setData(logedUser);

        } else {
            return new GenericResponse()
                    .setMessage("Username is not available")
                    .setCode(10);
        }
    }

    @GetMapping("me")
    public User me(HttpSession session) {
        return (User) session.getAttribute(LOGGEDIN_USER);
    }


}

