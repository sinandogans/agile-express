package com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateTaskModel {
    @NotNull
    private UUID projectId;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private int estimatedDuration;
    @NotBlank
    @Email
    private String emailOfCreator;
}
