package com.obss.jcp.sinandogan.agileexpress.domain.aggregates.email;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailTask {
    @Id
    @GeneratedValue
    private UUID id;
    private LocalDate date;
    private boolean isSent;
}
