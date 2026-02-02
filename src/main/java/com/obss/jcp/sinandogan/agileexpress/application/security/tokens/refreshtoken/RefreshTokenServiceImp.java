package com.obss.jcp.sinandogan.agileexpress.application.security.tokens.refreshtoken;

import com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces.RefreshTokenRepository;
import com.obss.jcp.sinandogan.agileexpress.application.security.tokens.jwt.CustomUserDetailsService;
import com.obss.jcp.sinandogan.agileexpress.application.security.tokens.jwt.JwtService;
import com.obss.jcp.sinandogan.agileexpress.application.services.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
class RefreshTokenServiceImp implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    RefreshTokenServiceImp(RefreshTokenRepository refreshTokenRepository, UserService userService, JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Transactional
    public RefreshToken createRefreshToken(String email, long refreshTokenDurationMs) {
        RefreshToken token = new RefreshToken(
                UUID.randomUUID().toString(),
                userService.findUserByEmail(email).getEmail(),
                Instant.now().plusMillis(refreshTokenDurationMs));
        refreshTokenRepository.deleteByEmail(email);
        return refreshTokenRepository.save(token);
    }

    public void verifyExpiration(RefreshToken token) {
        if (token.getExpirationDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired. Please login again.");
        }
    }

    public RefreshToken findByToken(String token) {
        var optionalToken = refreshTokenRepository.findByToken(token);
        if (optionalToken.isPresent()) {
            return optionalToken.get();
        }
        throw new RuntimeException("Refresh token not found");
    }

    public String refreshAccessToken(String refreshToken) {
        RefreshToken token = findByToken(refreshToken);
        verifyExpiration(token);
        return jwtService.generateToken(customUserDetailsService.loadUserByUsername(token.getEmail()));
    }

    @Override
    @Transactional
    public void deleteByEmail(String email) {
        refreshTokenRepository.deleteByEmail(email);
    }
}
