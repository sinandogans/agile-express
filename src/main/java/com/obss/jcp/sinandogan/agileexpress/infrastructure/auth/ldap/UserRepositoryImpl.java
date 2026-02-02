package com.obss.jcp.sinandogan.agileexpress.infrastructure.auth.ldap;

import com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces.UserRepository;
import com.obss.jcp.sinandogan.agileexpress.domain.pojo.User;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.auth.ldap.user.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final LdapUserRepository ldapUserRepository;

    public UserRepositoryImpl(LdapUserRepository ldapUserRepository) {
        this.ldapUserRepository = ldapUserRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return ldapUserRepository.findByUsername(username)
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return ldapUserRepository.findByEmail(email)
                .map(UserMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return ldapUserRepository.findAll().stream()
                .map(UserMapper::toDomain)
                .collect(Collectors.toList());
    }
}
