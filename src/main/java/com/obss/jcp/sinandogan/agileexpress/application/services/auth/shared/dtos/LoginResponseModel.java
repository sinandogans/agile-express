package com.obss.jcp.sinandogan.agileexpress.application.services.auth.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class LoginResponseModel {
    private String accessToken;
    private String refreshToken;
    private String title;
    private String email;
    private String firstName;
    private String lastName;
}
