package com.javaspringmvc.demo.config;

import com.javaspringmvc.demo.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class MyConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private UserConfigService userConfigService;

    @Autowired
    private CustomOAuth2UserService oauthUserService;
//    @Autowired
//    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    @Bean
    public UserDetailsService getUserDetailsService(){
        return new UserConfigService();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(getUserDetailsService());
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
//    @Override
//    protected AuthenticationManager authenticationManager() throws Exception {
//        return super.authenticationManager();
//    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth2/**")
                .permitAll()
                .antMatchers("/admin/**")
                .hasRole("ADMIN")
                .antMatchers("/**")
                .hasRole("USER")
                .antMatchers("/**")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/sign-in")
                .loginProcessingUrl("/login/post")
                .successHandler(authenticationSuccessHandler)
                .and()
                .logout()
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/sign-in")
                .userInfoEndpoint().userService(oauthUserService)
                .and()
                .successHandler(authenticationSuccessHandler)
                .and()
                .rememberMe()
                .key("mySecret")
                .userDetailsService(this.userConfigService)
                .and()
                .csrf()
                .disable();

    }

}
