package com.obss.jcp.sinandogan.agileexpress.webapi.controllers.user;

import com.obss.jcp.sinandogan.agileexpress.application.services.user.UserService;
import com.obss.jcp.sinandogan.agileexpress.application.services.user.dtos.UserResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        var response = userService.getAllUsers();
        var dtoResponse = response.stream().map(user -> modelMapper.map(user, UserResponseDto.class)).toList();
        return ResponseEntity.ok(dtoResponse);
    }
}
