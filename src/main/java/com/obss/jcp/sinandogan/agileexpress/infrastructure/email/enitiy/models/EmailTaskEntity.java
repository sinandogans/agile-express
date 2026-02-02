package com.obss.jcp.sinandogan.agileexpress.infrastructure.email.enitiy.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "email_task")
@AllArgsConstructor
@NoArgsConstructor
public class EmailTaskEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private LocalDate date;
    private boolean isSent;
}
