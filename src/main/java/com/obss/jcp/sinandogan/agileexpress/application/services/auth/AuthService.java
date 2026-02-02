package com.obss.jcp.sinandogan.agileexpress.application.services.auth;

import com.obss.jcp.sinandogan.agileexpress.application.services.auth.shared.dtos.UserDetailsDto;

public interface AuthService {
    Boolean isUserAdmin();

    String getTitle();

    UserDetailsDto getUserDetails(String email);
}
