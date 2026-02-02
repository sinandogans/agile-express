package com.obss.jcp.sinandogan.agileexpress.infrastructure.auth.oauth2;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.shared.dtos.LoginResponseModel;
import com.obss.jcp.sinandogan.agileexpress.domain.pojo.User;
import com.obss.jcp.sinandogan.agileexpress.application.security.tokens.jwt.CustomUserDetailsService;
import com.obss.jcp.sinandogan.agileexpress.application.security.tokens.jwt.JwtService;
import com.obss.jcp.sinandogan.agileexpress.application.security.tokens.refreshtoken.RefreshTokenService;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.google.CustomOAuth2Service;
import com.obss.jcp.sinandogan.agileexpress.application.services.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomOAuth2ServiceImp implements CustomOAuth2Service, OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Value("${google.client-id}")
    private String googleClientId;
    @com.google.api.client.util.Value("${refresh.token.expiration.remember.false}")
    private long refreshTokenNotRememberDuration;
    @com.google.api.client.util.Value("${refresh.token.expiration.remember.true}")
    private long refreshTokenRememberDuration;

    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService customUserDetailsService;

    public CustomOAuth2ServiceImp(UserService userService, JwtService jwtService, RefreshTokenService refreshTokenService, CustomUserDetailsService customUserDetailsService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        String email = oauth2User.getAttribute("email");
        if (!emailExistsInLdap(email)) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token"), "error message");
        }
        return oauth2User;
    }

    public LoginResponseModel googleLogin(String idToken) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        GoogleIdToken googleIdToken;
        try {
            googleIdToken = verifier.verify(idToken);
        } catch (Exception e) {
            throw new RuntimeException("Google token verification failed");
        }
        if (googleIdToken == null) {
            throw new RuntimeException("Google token verification failed");
        }
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        String email = payload.getEmail();
        if (!emailExistsInLdap(email)) {
            throw new RuntimeException("User not found");
        }
        User user = userService.findUserByEmail(email);
        //refactor
        long refreshTokenDuration = true ?
                refreshTokenRememberDuration :
                refreshTokenNotRememberDuration;
        return new LoginResponseModel(
                jwtService.generateToken(customUserDetailsService.loadUserByUsername(email)), refreshTokenService.createRefreshToken(email, refreshTokenDuration).getToken(), user.getTitle(), user.getEmail(), user.getFirstName(), user.getLastName());
    }

    private boolean emailExistsInLdap(String email) {
        return userService.emailExists(email);
    }
}
