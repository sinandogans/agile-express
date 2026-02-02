package com.obss.jcp.sinandogan.agileexpress.webapi.controllers.auth;

import com.obss.jcp.sinandogan.agileexpress.application.services.auth.google.CustomOAuth2Service;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.google.requests.login.GoogleLoginRequestDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.dtos.LoginResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/google")
public class GoogleAuthController {
    private final CustomOAuth2Service oAuth2Service;
    private final ModelMapper modelMapper;

    public GoogleAuthController(CustomOAuth2Service oAuth2Service, ModelMapper modelMapper) {
        this.oAuth2Service = oAuth2Service;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody GoogleLoginRequestDto googleLoginRequestDto, HttpServletResponse response) {
        var responseModel = oAuth2Service.googleLogin(googleLoginRequestDto.getIdToken());
        Cookie cookie = new Cookie("refresh_token", responseModel.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7days
        response.addCookie(cookie);
        return modelMapper.map(responseModel, LoginResponseDto.class);
    }
}
