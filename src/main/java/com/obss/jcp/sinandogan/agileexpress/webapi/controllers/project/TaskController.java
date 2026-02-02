package com.obss.jcp.sinandogan.agileexpress.webapi.controllers.project;

import com.obss.jcp.sinandogan.agileexpress.application.services.project.ProjectService;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.create.CreateTaskModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.logwork.LogWorkModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.movetosprint.MoveTaskToSprintModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.update.UpdateTaskModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.shared.TaskResponseDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.dtos.create.CreateTaskRequestDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.dtos.logwork.LogWorkRequestDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.dtos.update.UpdateTaskRequestDto;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.config.openapi.ApiErrorResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public class TaskController {
    private final ProjectService projectService;
    private final ModelMapper modelMapper;

    public TaskController(ProjectService projectService, ModelMapper modelMapper) {
        this.projectService = projectService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Create new task for the project.", description = "Admins, Project Manager, and Team Lead can create new task.")
    @ApiResponse(responseCode = "200", description = "New task created for the project.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDto.class)))
    @ApiErrorResponses({400, 401, 403, 404})
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(
            @PathVariable UUID projectId,
            @RequestBody CreateTaskRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        var createTaskModel = modelMapper.map(dto, CreateTaskModel.class);
        createTaskModel.setProjectId(projectId);
        createTaskModel.setEmailOfCreator(userDetails.getUsername());
        return ResponseEntity.ok(modelMapper.map(projectService.createTask(createTaskModel), TaskResponseDto.class));
    }

//    @Operation(summary = "Update task.", description = "Admins, Project Manager, Team Lead, and assigned Member can update task.")
//    @ApiResponse(responseCode = "200", description = "Task updated.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDto.class)))
//    @ApiErrorResponses({400, 401, 403, 404})
//    @PutMapping("/{taskId}")
//    public ResponseEntity<TaskResponseDto> updateTask(
//            @PathVariable UUID projectId,
//            @PathVariable UUID taskId,
//            @RequestBody UpdateTaskRequestDto dto,
//            @AuthenticationPrincipal UserDetails userDetails) {
//        var updateTaskModel = modelMapper.map(dto, UpdateTaskModel.class);
//        updateTaskModel.setTaskId(taskId);
//        updateTaskModel.setEmail(userDetails.getUsername());
//        return ResponseEntity.ok(modelMapper.map(projectService.updateTask(updateTaskModel), TaskResponseDto.class));
//    }

    @Operation(summary = "Delete task from the project.", description = "Admins, Project Manager, and Team Lead can delete task.")
    @ApiResponse(responseCode = "200", description = "Task deleted from the project.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiErrorResponses({400, 401, 403, 404})
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable UUID projectId,
            @PathVariable UUID taskId) {
        projectService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Log work for task.", description = "All project members can log work for their assigned tasks.")
    @ApiResponse(responseCode = "200", description = "Work logged successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiErrorResponses({400, 401, 403, 404})
    @PostMapping("/{taskId}/log-work")
    public ResponseEntity<Void> logWork(
            @PathVariable UUID projectId,
            @PathVariable UUID taskId,
            @RequestBody LogWorkRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        var logWorkModel = modelMapper.map(dto, LogWorkModel.class);
        logWorkModel.setTaskId(taskId);
        logWorkModel.setEmail(userDetails.getUsername());
        projectService.logWork(logWorkModel);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Move task to sprint.", description = "Admins, Project Manager, and Team Lead can move task to sprint.")
    @ApiResponse(responseCode = "200", description = "Task moved to sprint.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDto.class)))
    @ApiErrorResponses({400, 401, 403, 404})
    @PutMapping("/{taskId}/move-to-sprint/{sprintId}")
    public ResponseEntity<TaskResponseDto> moveTaskToSprint(
            @PathVariable UUID projectId,
            @PathVariable UUID taskId,
            @PathVariable UUID sprintId) {
        return ResponseEntity.ok(modelMapper.map(projectService.moveTaskToSprint(taskId, sprintId), TaskResponseDto.class));
    }

    @Operation(summary = "Remove task from sprint.", description = "Admins, Project Manager, and Team Lead can remove task from sprint.")
    @ApiResponse(responseCode = "200", description = "Task removed from sprint.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDto.class)))
    @ApiErrorResponses({400, 401, 403, 404})
    @DeleteMapping("/{taskId}/sprint")
    public ResponseEntity<TaskResponseDto> removeTaskFromSprint(
            @PathVariable UUID projectId,
            @PathVariable UUID taskId) {
        return ResponseEntity.ok(modelMapper.map(projectService.removeTaskFromSprint(taskId), TaskResponseDto.class));
    }
}
