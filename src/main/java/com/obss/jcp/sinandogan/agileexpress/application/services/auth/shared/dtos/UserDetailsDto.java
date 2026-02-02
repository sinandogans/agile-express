package com.obss.jcp.sinandogan.agileexpress.application.services.auth.shared.dtos;

import com.obss.jcp.sinandogan.agileexpress.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto {
    private String email;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private Role role;
}
