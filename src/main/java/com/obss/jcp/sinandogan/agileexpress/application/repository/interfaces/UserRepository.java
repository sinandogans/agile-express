package com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces;

import com.obss.jcp.sinandogan.agileexpress.domain.pojo.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAll();
}
