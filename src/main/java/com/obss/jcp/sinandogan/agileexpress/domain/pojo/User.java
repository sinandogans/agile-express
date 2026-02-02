package com.obss.jcp.sinandogan.agileexpress.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.naming.Name;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Name id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String title;
//    private String password;
}
