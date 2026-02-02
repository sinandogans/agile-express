package com.obss.jcp.sinandogan.agileexpress.webapi.controllers.project;

import com.obss.jcp.sinandogan.agileexpress.application.services.project.ProjectService;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.shared.SprintResponseDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.sprint.models.start.StartNextSprintModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.sprint.models.dtos.start.StartNextSprintRequestDto;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.config.openapi.ApiErrorResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/projects/{projectId}/sprints")
public class SprintController {
    private final ProjectService projectService;
    private final ModelMapper modelMapper;

    public SprintController(ProjectService projectService, ModelMapper modelMapper) {
        this.projectService = projectService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Create new sprint for the project.", description = "Admins, Project Manager, and Team Lead can create new sprint.")
    @ApiResponse(responseCode = "200", description = "New sprint created for the project.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SprintResponseDto.class))))
    @ApiErrorResponses({400, 401, 403, 404})
    @PostMapping
    public ResponseEntity<SprintResponseDto> createSprint(@PathVariable UUID projectId) {
        return ResponseEntity.ok(modelMapper.map(projectService.createSprint(projectId), SprintResponseDto.class));
    }

    @Operation(summary = "Delete the sprint from the project.", description = "Admins, Project Manager, and Team Lead can delete the sprint.")
    @ApiResponse(responseCode = "200", description = "Sprint deleted from the project.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Void.class))))
    @ApiErrorResponses({400, 401, 403, 404})
    @DeleteMapping("/{sprintNumber}")
    public ResponseEntity<Void> deleteSprint(
            @PathVariable UUID projectId,
            @PathVariable int sprintNumber) {
        projectService.deleteSprint(projectId, sprintNumber);
        return ResponseEntity.ok().build();
    }

//    @Operation(summary = "Start the next sprint of the project.", description = "Admins, Project Manager, and Team Lead can start the next sprint.")
//    @ApiResponse(responseCode = "200", description = "Next sprint of the project started.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Void.class))))
//    @ApiErrorResponses({400, 401, 403})
//    @PostMapping("/start")
//    public ResponseEntity<Void> startNextSprint(
//            @PathVariable UUID projectId) {
//        projectService.startNextSprint(projectId);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/end")
//    public ResponseEntity<Void> endCurrentSprint(@PathVariable UUID projectId) {
//        projectService.endCurrentSprint(projectId);
//        return ResponseEntity.ok().build();
//    }
}
