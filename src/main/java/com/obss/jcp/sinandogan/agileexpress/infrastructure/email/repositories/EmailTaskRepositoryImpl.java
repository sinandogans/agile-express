package com.obss.jcp.sinandogan.agileexpress.infrastructure.email.repositories;

import com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces.EmailTaskRepository;
import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.email.EmailTask;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.email.repositories.jpa.JPAEmailTaskRepository;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.email.mappers.EmailTaskMapper;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.email.enitiy.models.EmailTaskEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public class EmailTaskRepositoryImpl implements EmailTaskRepository {
    private final JPAEmailTaskRepository jpaEmailTaskRepository;

    public EmailTaskRepositoryImpl(JPAEmailTaskRepository jpaEmailTaskRepository) {
        this.jpaEmailTaskRepository = jpaEmailTaskRepository;
    }

    @Override
    public Optional<EmailTask> findByDate(LocalDate date) {
        return jpaEmailTaskRepository.findByDate(date)
                .map(EmailTaskMapper::toDomain);
    }

    @Override
    public EmailTask save(EmailTask emailTask) {
        EmailTaskEntity entity = EmailTaskMapper.toEntity(emailTask);
        jpaEmailTaskRepository.save(entity);
        return EmailTaskMapper.toDomain(entity);
    }
}
