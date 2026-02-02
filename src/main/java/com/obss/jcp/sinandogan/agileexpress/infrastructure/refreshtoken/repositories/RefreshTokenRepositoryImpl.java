package com.obss.jcp.sinandogan.agileexpress.infrastructure.refreshtoken.repositories;

import com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces.RefreshTokenRepository;
import com.obss.jcp.sinandogan.agileexpress.application.security.tokens.refreshtoken.RefreshToken;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.refreshtoken.mappers.RefreshTokenMapper;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.refreshtoken.entity.models.RefreshTokenEntity;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.refreshtoken.repositories.jpa.JpaRefreshTokenRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final JpaRefreshTokenRepository refreshTokenRepository;

    public RefreshTokenRepositoryImpl(JpaRefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenEntity entity = RefreshTokenMapper.toEntity(refreshToken);
        refreshTokenRepository.save(entity);
        return RefreshTokenMapper.toDomain(entity);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(RefreshTokenMapper::toDomain);
    }

    @Override
    public void deleteByEmail(String email) {
        refreshTokenRepository.deleteByEmail(email);
    }

    @Override
    public void delete(RefreshToken token) {
        refreshTokenRepository.delete(RefreshTokenMapper.toEntity(token));
    }
}
