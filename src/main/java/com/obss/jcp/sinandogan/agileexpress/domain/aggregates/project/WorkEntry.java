package com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class WorkEntry {
    private final LocalDate date;
    private final int hours;

    public WorkEntry(LocalDate date, int hours) {
        if (hours < 0) {
            throw new IllegalArgumentException("Hours cannot be negative.");
        }
        this.date = date;
        this.hours = hours;
    }

}
