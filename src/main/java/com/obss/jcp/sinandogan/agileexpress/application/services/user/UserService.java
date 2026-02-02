package com.obss.jcp.sinandogan.agileexpress.application.services.user;

import com.obss.jcp.sinandogan.agileexpress.application.services.user.models.shared.UserResponseModel;
import com.obss.jcp.sinandogan.agileexpress.domain.pojo.User;

import java.util.List;

public interface UserService {
    List<UserResponseModel> getAllUsers();

    User findUserByEmail(String email);

    boolean emailExists(String email);
}
