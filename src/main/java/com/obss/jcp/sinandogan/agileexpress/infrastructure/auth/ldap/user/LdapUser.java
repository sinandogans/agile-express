package com.obss.jcp.sinandogan.agileexpress.infrastructure.auth.ldap.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entry(base = "ou=users", objectClasses = {"inetOrgPerson", "top"})
public class LdapUser {
    @Id
    private Name id;

    @Attribute(name = "cn")
    private String username;

    @Attribute(name = "mail")
    private String email;

    @Attribute(name = "givenname")
    private String firstname;

    @Attribute(name = "sn")
    private String lastname;

    @Attribute(name = "title")
    private String title;

//    @Attribute(name = "userpassword")
//    private String password;

}
