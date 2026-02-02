package com.obss.jcp.sinandogan.agileexpress.infrastructure.refreshtoken.repositories.jpa;

import com.obss.jcp.sinandogan.agileexpress.infrastructure.refreshtoken.entity.models.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaRefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    Optional<RefreshTokenEntity> findByToken(String token);

    void deleteByEmail(String email);
}
