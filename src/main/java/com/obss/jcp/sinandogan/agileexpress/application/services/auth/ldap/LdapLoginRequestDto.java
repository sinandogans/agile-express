package com.obss.jcp.sinandogan.agileexpress.application.services.auth.ldap;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LdapLoginRequestDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private boolean rememberMe;
}
