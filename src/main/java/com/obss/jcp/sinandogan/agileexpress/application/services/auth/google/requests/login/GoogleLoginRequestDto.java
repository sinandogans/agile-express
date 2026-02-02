package com.obss.jcp.sinandogan.agileexpress.application.services.auth.google.requests.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class GoogleLoginRequestDto {
    private String idToken;
}
