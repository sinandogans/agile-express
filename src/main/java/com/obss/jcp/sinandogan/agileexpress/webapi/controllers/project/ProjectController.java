package com.obss.jcp.sinandogan.agileexpress.webapi.controllers.project;

import com.obss.jcp.sinandogan.agileexpress.application.services.project.ProjectService;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.addmembers.AddMembersRequestDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.removemembers.RemoveMembersRequestDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.update.*;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.create.CreateProjectRequestDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.create.CreateProjectResponseDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.shared.ProjectResponseDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.summary.ProjectSummaryResponseDto;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.config.openapi.ApiErrorResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final ModelMapper modelMapper;

    public ProjectController(ProjectService projectService, ModelMapper modelMapper) {
        this.projectService = projectService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get projects of signed-in user", description = "Only related(Manager/Team Lead/Member of the Project) signed-in users could get the projects.")
    @ApiResponse(responseCode = "200", description = "Projects of the signed-in user fetched.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProjectResponseDto.class))))
    @ApiErrorResponses({400, 401, 403})
    @GetMapping()
    public ResponseEntity<List<ProjectResponseDto>> getProjectsOfUser(@AuthenticationPrincipal UserDetails userDetails) {
        var response = projectService.getProjectsOfUser(userDetails.getUsername());
        var dtoResponse = response.stream().map(project -> modelMapper.map(project, ProjectResponseDto.class)).toList();
        return ResponseEntity.ok(dtoResponse);
    }

    @Operation(summary = "Get project details by ID", description = "Only related(Manager/Team Lead/Member of the Project) signed-in users could get the project detail.")
    @ApiResponse(responseCode = "200", description = "Project details found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDto.class)))
    @ApiErrorResponses({400, 401, 403})
    @GetMapping("{projectId}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable UUID projectId, @AuthenticationPrincipal UserDetails userDetails) {
        var response = projectService.getProjectOfUserByProjectId(projectId, userDetails.getUsername());
        return ResponseEntity.ok(modelMapper.map(response, ProjectResponseDto.class));
    }

    @Operation(summary = "Get summary of projects of signed-in user", description = "Only related(Manager/Team Lead/Member of the Project) signed-in users could get the summary of projects.")
    @ApiResponse(responseCode = "200", description = "Summary of projects of the signed-in user fetched.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProjectSummaryResponseDto.class))))
    @ApiErrorResponses({400, 401, 403})
    @GetMapping("/summary")
    public ResponseEntity<List<ProjectSummaryResponseDto>> getSummaryOfProjectsOfUser(@AuthenticationPrincipal UserDetails userDetails) {
        var response = projectService.getSummaryOfProjectsOfUser(userDetails.getUsername());
        var dtoResponse = response.stream().map(summary -> modelMapper.map(summary, ProjectSummaryResponseDto.class)).toList();
        return ResponseEntity.ok(dtoResponse);
    }

    @Operation(summary = "Create new project.", description = "Only Admins and Project Managers can create new project.")
    @ApiResponse(responseCode = "200", description = "Project created.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateProjectResponseDto.class)))
    @ApiErrorResponses({400, 401, 409})
    @PostMapping("/create")
    public ResponseEntity<CreateProjectResponseDto> createProject(@RequestBody @Valid CreateProjectRequestDto dto) {
        return ResponseEntity.ok(projectService.createProject(dto));
    }

    @Operation(summary = "Delete the project.", description = "Only Admins can delete project.")
    @ApiResponse(responseCode = "200", description = "Project deleted.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiErrorResponses({400, 401, 404})
    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID projectId) {
        this.projectService.deleteProject(projectId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update Team Lead of the project.", description = "Admins and Project Manager can update the Team Lead.")
    @ApiResponse(responseCode = "200", description = "Team Lead updated.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDto.class)))
    @ApiErrorResponses({400, 401, 404})
    @PutMapping("/updateTeamLead")
    public ResponseEntity<ProjectResponseDto> updateTeamLead(@RequestBody @Valid UpdateTeamLeadRequestDto dto) {
        return ResponseEntity.ok(modelMapper.map(projectService.updateTeamLead(dto), ProjectResponseDto.class));
    }

    @Operation(summary = "Update Manager of the project.", description = "Only Admins can update the Manager.")
    @ApiResponse(responseCode = "200", description = "Manager updated.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDto.class)))
    @ApiErrorResponses({400, 401, 404})
    @PutMapping("/updateManager")
    public ResponseEntity<ProjectResponseDto> updateManager(@RequestBody @Valid UpdateManagerRequestDto dto) {
        return ResponseEntity.ok(modelMapper.map(projectService.updateManager(dto), ProjectResponseDto.class));
    }

    @Operation(summary = "Update name of the project.", description = "Admins and Project Manager can update the name.")
    @ApiResponse(responseCode = "200", description = "Project's name updated.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDto.class)))
    @ApiErrorResponses({400, 401, 404})
    @PutMapping("/updateName")
    public ResponseEntity<ProjectResponseDto> updateName(@RequestBody @Valid UpdateNameRequestDto dto) {
        return ResponseEntity.ok(modelMapper.map(projectService.updateName(dto), ProjectResponseDto.class));
    }

    @Operation(summary = "Update description of the project.", description = "Admins and Project Manager can update the description.")
    @ApiResponse(responseCode = "200", description = "Project's description updated.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDto.class)))
    @ApiErrorResponses({400, 401, 404})
    @PutMapping("/updateDescription")
    public ResponseEntity<ProjectResponseDto> updateDescription(@RequestBody @Valid UpdateDescriptionRequestDto dto) {
        return ResponseEntity.ok(modelMapper.map(projectService.updateDescription(dto), ProjectResponseDto.class));
    }

    @Operation(summary = "Update start date of the project.", description = "Admins and Project Manager can update the start date.")
    @ApiResponse(responseCode = "200", description = "Project's start date updated.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDto.class)))
    @ApiErrorResponses({400, 401, 404})
    @PutMapping("/updateStartDate")
    public ResponseEntity<ProjectResponseDto> updateStartDate(@RequestBody @Valid UpdateStartDateRequestDto dto) {
        return ResponseEntity.ok(modelMapper.map(projectService.updateStartDate(dto), ProjectResponseDto.class));
    }

    @Operation(summary = "Update end date of the project.", description = "Admins and Project Manager can update the end date.")
    @ApiResponse(responseCode = "200", description = "Project's end date updated.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDto.class)))
    @ApiErrorResponses({400, 401, 404})
    @PutMapping("/updateEndDate")
    public ResponseEntity<ProjectResponseDto> updateEndDate(@RequestBody @Valid UpdateEndDateRequestDto dto) {
        return ResponseEntity.ok(modelMapper.map(projectService.updateEndDate(dto), ProjectResponseDto.class));
    }

    @Operation(summary = "Update sprint duration of the project.", description = "Admins and Project Manager can update the sprint duration.")
    @ApiResponse(responseCode = "200", description = "Project's sprint duration updated.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDto.class)))
    @ApiErrorResponses({400, 401, 404})
    @PutMapping("/updateSprintDuration")
    public ResponseEntity<ProjectResponseDto> updateSprintDuration(@RequestBody @Valid UpdateSprintDurationRequestDto dto) {
        return ResponseEntity.ok(modelMapper.map(projectService.updateSprintDuration(dto), ProjectResponseDto.class));
    }

    @Operation(summary = "Add members to the project.", description = "Admins and Project Manager can add members to the project.")
    @ApiResponse(responseCode = "200", description = "New members added to the project.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDto.class)))
    @ApiErrorResponses({400, 401, 404})
    @PutMapping("/addMembers")
    public ResponseEntity<ProjectResponseDto> addMembers(@RequestBody @Valid AddMembersRequestDto dto) {
        return ResponseEntity.ok(modelMapper.map(projectService.addMembers(dto), ProjectResponseDto.class));
    }

    @Operation(summary = "Remove members from the project.", description = "Admins and Project Manager can remove members from the project.")
    @ApiResponse(responseCode = "200", description = "Members removed from the project.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDto.class)))
    @ApiErrorResponses({400, 401, 404})
    @PutMapping("/removeMembers")
    public ResponseEntity<ProjectResponseDto> removeMembers(@RequestBody @Valid RemoveMembersRequestDto dto) {
        return ResponseEntity.ok(modelMapper.map(projectService.removeMembers(dto), ProjectResponseDto.class));
    }

}
