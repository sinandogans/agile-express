package com.obss.jcp.sinandogan.agileexpress.application.services.project.models.delete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeleteProjectResponseModel {
    UUID projectId;
}
