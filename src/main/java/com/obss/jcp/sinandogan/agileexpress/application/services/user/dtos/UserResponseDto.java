package com.obss.jcp.sinandogan.agileexpress.application.services.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.naming.Name;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDto {
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String title;
}
