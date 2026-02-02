package com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.dtos.create;

import com.obss.jcp.sinandogan.agileexpress.domain.enums.StoryPoint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateTaskRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private int estimatedDuration;
}
