package com.obss.jcp.sinandogan.agileexpress.webapi.controllers.auth;

import com.obss.jcp.sinandogan.agileexpress.application.services.auth.ldap.LdapLoginRequestDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.ldap.LdapLoginService;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.dtos.LoginResponseDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.shared.dtos.LoginResponseModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.shared.ProjectResponseDto;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.config.openapi.ApiErrorResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/ldap")
public class LdapAuthController {
    private final LdapLoginService ldapLoginService;
    private final ModelMapper modelMapper;

    @Value("${refresh.token.expiration.standart}")
    private long refreshTokenDuration;
    @Value("${refresh.token.expiration.remember}")
    private long rememberMeDuration;

    public LdapAuthController(LdapLoginService ldapLoginService, ModelMapper modelMapper) {
        this.ldapLoginService = ldapLoginService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Sign-in using LDAP credentials.", description = "")
    @ApiResponse(responseCode = "200", description = "Signed-in successfully.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LoginResponseDto.class))))
    @ApiErrorResponses({400, 404})
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LdapLoginRequestDto ldapLoginRequestDto) {
        LoginResponseModel responseModel = ldapLoginService.ldapLogin(ldapLoginRequestDto);
        long cookieMaxAge = ldapLoginRequestDto.isRememberMe() ?
                rememberMeDuration :
                refreshTokenDuration;

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", responseModel.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/api/auth/refresh")
                .maxAge(cookieMaxAge / 1000)
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshCookie.toString()).body(modelMapper.map(responseModel, LoginResponseDto.class));
    }
}
