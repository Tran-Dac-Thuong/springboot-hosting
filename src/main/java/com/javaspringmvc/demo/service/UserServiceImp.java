package com.javaspringmvc.demo.service;


import com.javaspringmvc.demo.config.SecurityUtils;
import com.javaspringmvc.demo.config.UserConfigService;
import com.javaspringmvc.demo.model.AuthenticationProvider;
import com.javaspringmvc.demo.model.User;
import com.javaspringmvc.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean checkUsername(String firstName, String lastName) {
        return userRepository.existsByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    public User checkUserEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        return user;

    }

//    @Override
//    public Map<String, Object> getOauthUser(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
//        if(oAuth2AuthenticationToken.getPrincipal().getAttributes() == null){
//            return null;
//        }else{
//            return oAuth2AuthenticationToken.getPrincipal().getAttributes();
//        }
//
//    }


}
