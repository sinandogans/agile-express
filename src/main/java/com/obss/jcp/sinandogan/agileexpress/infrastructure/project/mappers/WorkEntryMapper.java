package com.obss.jcp.sinandogan.agileexpress.infrastructure.project.mappers;

import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project.WorkEntry;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.project.entity.models.WorkEntryEntity;

public class WorkEntryMapper {

    public static WorkEntry toDomain(WorkEntryEntity entity) {
        if (entity == null) return null;
        return new WorkEntry(entity.getDate(), entity.getHours());
    }

    public static WorkEntryEntity toEntity(WorkEntry domain) {
        if (domain == null) return null;
        WorkEntryEntity entity = new WorkEntryEntity();
        entity.setDate(domain.getDate());
        entity.setHours(domain.getHours());
        return entity;
    }
}
