package com.obss.jcp.sinandogan.agileexpress.infrastructure.email.repositories.jpa;

import com.obss.jcp.sinandogan.agileexpress.infrastructure.email.enitiy.models.EmailTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JPAEmailTaskRepository extends JpaRepository<EmailTaskEntity, UUID> {
    Optional<EmailTaskEntity> findByDate(LocalDate date);
}
