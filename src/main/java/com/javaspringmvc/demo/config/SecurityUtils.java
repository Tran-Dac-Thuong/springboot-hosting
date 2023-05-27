package com.javaspringmvc.demo.config;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class SecurityUtils {

    public static MyUser getPrincipal() {
        return (MyUser) (SecurityContextHolder.getContext()).getAuthentication().getPrincipal();
    }
}
