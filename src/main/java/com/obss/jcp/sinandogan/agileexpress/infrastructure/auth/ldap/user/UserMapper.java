package com.obss.jcp.sinandogan.agileexpress.infrastructure.auth.ldap.user;

import com.obss.jcp.sinandogan.agileexpress.domain.pojo.User;

public class UserMapper {
    public static User toDomain(LdapUser user) {
        return new User(user.getId(), user.getUsername(), user.getEmail(), user.getFirstname(), user.getLastname(), user.getTitle());
    }

    public static LdapUser toLdapUser(User user) {
        return new LdapUser(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getTitle());
    }
}
