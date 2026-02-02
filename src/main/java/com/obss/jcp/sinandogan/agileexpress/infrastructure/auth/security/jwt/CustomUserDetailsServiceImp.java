package com.obss.jcp.sinandogan.agileexpress.infrastructure.auth.security.jwt;

import com.obss.jcp.sinandogan.agileexpress.application.security.tokens.jwt.CustomUserDetailsService;
import com.obss.jcp.sinandogan.agileexpress.application.services.user.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsServiceImp implements CustomUserDetailsService, UserDetailsService {
    private final UserService userService;

    public CustomUserDetailsServiceImp(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userService.findUserByEmail(username);
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getTitle()));

        return new User(
                user.getEmail(),    // username
                "",                 // password
                authorities
        );
    }
}
