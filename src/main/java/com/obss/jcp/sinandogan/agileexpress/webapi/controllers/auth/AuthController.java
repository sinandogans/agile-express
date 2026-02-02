package com.obss.jcp.sinandogan.agileexpress.webapi.controllers.auth;

import com.obss.jcp.sinandogan.agileexpress.application.security.tokens.refreshtoken.RefreshTokenService;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.AuthService;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.dtos.LoginResponseDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.dtos.RefreshDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.shared.dtos.UserDetailsDto;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.config.openapi.ApiErrorResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;

    public AuthController(RefreshTokenService refreshTokenService, AuthService authService) {
        this.refreshTokenService = refreshTokenService;
        this.authService = authService;
    }

    @Operation(summary = "Refresh access token using Refresh Token.", description = "")
    @ApiResponse(responseCode = "200", description = "Access token refreshed successfully.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LoginResponseDto.class))))
    @ApiErrorResponses({400, 404})
    @PostMapping("/refresh")
    public RefreshDto refreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new RuntimeException("No refresh token cookie found");
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Refresh token not found in cookies"));

        String accessToken = refreshTokenService.refreshAccessToken(refreshToken);
        return new RefreshDto(accessToken);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        refreshTokenService.deleteByEmail(email);
        //killer token
        ResponseCookie cleanCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cleanCookie.toString()).build();
    }

    @GetMapping("/user")
    public ResponseEntity<UserDetailsDto> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        var response = authService.getUserDetails(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/is-user-admin")
    public ResponseEntity<Boolean> isUserAdmin() {
        return ResponseEntity.ok(authService.isUserAdmin());
    }

    @GetMapping("/title")
    public ResponseEntity<String> getTitle() {
        return ResponseEntity.ok(authService.getTitle());
    }
}
