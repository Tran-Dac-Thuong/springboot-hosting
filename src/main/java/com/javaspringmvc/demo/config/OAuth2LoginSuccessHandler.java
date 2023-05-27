//package com.javaspringmvc.demo.config;
//
//import com.javaspringmvc.demo.model.AuthenticationProvider;
//import com.javaspringmvc.demo.model.User;
//import com.javaspringmvc.demo.service.CustomOAuth2User;
//import com.javaspringmvc.demo.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Primary;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//@Component
//public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
//    @Autowired
//    private UserService userService;
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
//        String email = customOAuth2User.getEmail();
//        String name = customOAuth2User.getName();
//        User user = userService.checkUserEmail(email);
//        if(user == null){
//            userService.createNewUserAfterOAuthLoginSuccess(email, name, AuthenticationProvider.GOOGLE);
//        }else{
//            userService.updateUserAfterOAuthLoginSuccess(user, name, AuthenticationProvider.GOOGLE);
//        }
//
//    }
//
//
//
//}
