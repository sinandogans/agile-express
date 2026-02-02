package com.obss.jcp.sinandogan.agileexpress.application.services.auth.dtos;

import lombok.*;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class LoginResponseDto {
    private String accessToken;
    private String title;
    private String email;
    private String firstName;
    private String lastName;
}
