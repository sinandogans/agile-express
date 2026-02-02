package com.obss.jcp.sinandogan.agileexpress.infrastructure.auth.ldap.user;

import com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces.UserRepository;
import com.obss.jcp.sinandogan.agileexpress.application.services.user.UserService;
import com.obss.jcp.sinandogan.agileexpress.application.services.user.excepitons.UserNotFoundException;
import com.obss.jcp.sinandogan.agileexpress.application.services.user.models.shared.UserResponseModel;
import com.obss.jcp.sinandogan.agileexpress.domain.pojo.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImp(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserResponseModel> getAllUsers() {
        return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserResponseModel.class)).toList();
    }

    @Override
    public User findUserByEmail(String email) {
        var optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UserNotFoundException("User not found", email);
    }

    @Override
    public boolean emailExists(String email) {
        var optionalUser = userRepository.findByEmail(email);
        return optionalUser.isPresent();
    }
}
