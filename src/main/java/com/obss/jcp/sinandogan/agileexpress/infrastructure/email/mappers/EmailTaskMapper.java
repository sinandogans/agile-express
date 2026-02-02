package com.obss.jcp.sinandogan.agileexpress.infrastructure.email.mappers;

import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.email.EmailTask;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.email.enitiy.models.EmailTaskEntity;

public class EmailTaskMapper {
    public static EmailTask toDomain(EmailTaskEntity entity) {
        if (entity == null) return null;
        return new EmailTask(entity.getId(), entity.getDate(), entity.isSent());
    }

    public static EmailTaskEntity toEntity(EmailTask domain) {
        if (domain == null) return null;
        EmailTaskEntity entity = new EmailTaskEntity();
        entity.setDate(domain.getDate());
        entity.setId(domain.getId());
        entity.setSent(domain.isSent());
        return entity;
    }
}
