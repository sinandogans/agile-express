package com.obss.jcp.sinandogan.agileexpress.infrastructure.auth;

import com.obss.jcp.sinandogan.agileexpress.application.services.auth.AuthService;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.shared.dtos.UserDetailsDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.user.UserService;
import com.obss.jcp.sinandogan.agileexpress.domain.enums.Role;
import com.obss.jcp.sinandogan.agileexpress.domain.pojo.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements AuthService {
    private final UserService userService;

    public AuthServiceImp(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Boolean isUserAdmin() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String title = userService.findUserByEmail(email).getTitle();
        return title.equals("admin");
    }

    @Override
    public String getTitle() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByEmail(email).getTitle();
    }

    @Override
    public UserDetailsDto getUserDetails(String email) {
        User user = userService.findUserByEmail(email);
        return new UserDetailsDto(user.getEmail(), user.getFirstName(), user.getLastName(), "", Role.fromValue(user.getTitle()));
    }
}
