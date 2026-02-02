package com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.dtos.delete;

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
public class DeleteTaskRequestDto {
    @NotNull
    private UUID taskId;
}
