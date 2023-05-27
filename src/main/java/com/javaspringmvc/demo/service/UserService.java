package com.javaspringmvc.demo.service;


import com.javaspringmvc.demo.model.AuthenticationProvider;
import com.javaspringmvc.demo.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


public interface UserService {
    public boolean checkEmail(String email);

    public boolean checkUsername(String firstName, String lastName);

    public User checkUserEmail(String email);

    public User getCurrentUser();

//    public Map<String, Object> getOauthUser(OAuth2AuthenticationToken oAuth2AuthenticationToken);


}
