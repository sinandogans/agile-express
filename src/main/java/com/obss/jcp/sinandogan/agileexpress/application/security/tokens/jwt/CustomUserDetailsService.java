package com.obss.jcp.sinandogan.agileexpress.application.security.tokens.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailsService {
    UserDetails loadUserByUsername(String username);
}
