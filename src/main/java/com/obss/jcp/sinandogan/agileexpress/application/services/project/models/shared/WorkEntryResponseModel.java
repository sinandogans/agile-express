package com.obss.jcp.sinandogan.agileexpress.application.services.project.models.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkEntryResponseModel {
    private int hours;
    private LocalDate date;
}
