package com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces;

import com.obss.jcp.sinandogan.agileexpress.application.security.tokens.refreshtoken.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);

    void deleteByEmail(String email);

    void delete(RefreshToken token);
}
