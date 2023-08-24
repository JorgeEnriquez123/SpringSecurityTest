package com.jorge.securitytest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/session")
public class SessionController {
    @Autowired
    SessionRegistry sessionRegistry;

    @GetMapping("/info")
    public Object sessionInfo(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<SessionInformation> session = sessionRegistry.getAllSessions(user, true);
        return session;
    }

    @GetMapping("/expired")
    public String expiredSession(){
        return "Your session has expired! " +
                "This may have happened because you signed in on another device " +
                "So your previous session (this one) is now expired.";
    }
    /*@GetMapping("/invalid")
    public String invalidSession(){
        return "Your session has become invalid!\n" +
                "This may have happened because your current session ended due to a timeout or something else";
    }*/
}
