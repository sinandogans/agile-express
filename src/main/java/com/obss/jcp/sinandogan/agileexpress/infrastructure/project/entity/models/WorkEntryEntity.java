package com.obss.jcp.sinandogan.agileexpress.infrastructure.project.entity.models;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Embeddable
public class WorkEntryEntity {
    private LocalDate date;
    private int hours;
}
