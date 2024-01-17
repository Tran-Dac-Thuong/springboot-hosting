package com.javaspringmvc.demo.service;


import com.javaspringmvc.demo.model.User;
import com.javaspringmvc.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class AuthService {

    @Autowired
    private UserRepository userRepo;



    @Autowired
    private JwrService jwtService;


    public User isAuthenticatedUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    String username = jwtService.extractUsername(cookie.getValue());
                    User user = userRepo.findByEmail(username);
                    return user;
                }
            }
        }
        return null;
    }

}
