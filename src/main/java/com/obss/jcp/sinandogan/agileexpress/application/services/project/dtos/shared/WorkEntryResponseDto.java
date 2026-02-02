package com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkEntryResponseDto {
    private LocalDate date;
    private int hours;
}
