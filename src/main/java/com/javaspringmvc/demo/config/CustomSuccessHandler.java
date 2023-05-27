package com.javaspringmvc.demo.config;

import com.javaspringmvc.demo.model.AuthenticationProvider;
import com.javaspringmvc.demo.model.User;
import com.javaspringmvc.demo.repository.UserRepository;
import com.javaspringmvc.demo.service.CustomOAuth2User;
import com.javaspringmvc.demo.service.CustomOAuth2UserService;
import com.javaspringmvc.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Configuration
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } else {
//            response.sendRedirect("/");
            String redirectUrl = null;
            if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
                DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
                String username = userDetails.getAttribute("email") != null ? userDetails.getAttribute("email") : userDetails.getAttribute("login") + "@gmail.com";
                if (userService.checkUserEmail(username) == null) {
                    User user = new User();
                    user.setEmail(username);
                    user.setFirstName(userDetails.getAttribute("name") != null ? userDetails.getAttribute("name") : userDetails.getAttribute("login"));
                    user.setLastName(userDetails.getAttribute("name") != null ? userDetails.getAttribute("name") : userDetails.getAttribute("login"));
                    user.setUsername(userDetails.getAttribute("sub") != null ? userDetails.getAttribute("sub") : userDetails.getAttribute("login"));
                    user.setPassword("Password of google account");
                    user.setRole("ROLE_USER");
                    user.setAvatar(userDetails.getAttribute("picture") != null ? userDetails.getAttribute("picture") : userDetails.getAttribute("login"));
                    user.setAuthenticationProvider(AuthenticationProvider.GOOGLE);
                    userRepository.save(user);
                } else {
                    User currentUser = userService.checkUserEmail(username);
                    currentUser.setFirstName(userDetails.getAttribute("name") != null ? userDetails.getAttribute("name") : userDetails.getAttribute("login"));
                    currentUser.setLastName(userDetails.getAttribute("name") != null ? userDetails.getAttribute("name") : userDetails.getAttribute("login"));
                    currentUser.setUsername(userDetails.getAttribute("sub") != null ? userDetails.getAttribute("sub") : userDetails.getAttribute("login"));
                    currentUser.setAuthenticationProvider(AuthenticationProvider.GOOGLE);
                    currentUser.setAvatar(userDetails.getAttribute("picture") != null ? userDetails.getAttribute("picture") : userDetails.getAttribute("login"));
                    userRepository.save(currentUser);
                }

            }
            redirectUrl = "/";
            new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);


        }


    }
}
