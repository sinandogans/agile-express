package com.obss.jcp.sinandogan.agileexpress.application.services.project;

import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.addmembers.AddMembersRequestDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.create.CreateProjectRequestDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.create.CreateProjectResponseDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.removemembers.RemoveMembersRequestDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.update.*;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.models.shared.ProjectResponseModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.models.shared.SprintResponseModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.models.shared.TaskResponseModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.models.summary.ProjectSummaryResponseModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.create.CreateTaskModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.logwork.LogWorkModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.movetosprint.MoveTaskToSprintModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.update.UpdateTaskModel;
import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project.Project;
import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project.Task;

import java.util.List;
import java.util.UUID;

public interface ProjectService {
    CreateProjectResponseDto createProject(CreateProjectRequestDto createProjectRequestDto);

    SprintResponseModel createSprint(UUID projectId);

    TaskResponseModel createTask(CreateTaskModel createTaskModel);

    ProjectResponseModel getProjectOfUserByProjectId(UUID projectId, String email);

    void deleteTask(UUID taskId);

    Project getById(UUID projectId);

    Project getProjectById(UUID projectId);

    Task getTaskById(UUID taskId);

    void deleteProject(UUID projectId);

    TaskResponseModel moveTaskToSprint(UUID taskId, UUID sprintId);

    TaskResponseModel removeTaskFromSprint(UUID taskId);

    TaskResponseModel updateTask(UpdateTaskModel updateTaskModel);

    Project getProjectByTaskId(UUID taskId);

    Project getProjectByBacklogId(UUID backlogId);

    Project getProjectBySprintId(UUID sprintId);

    List<ProjectResponseModel> getProjectsOfUser(String email);

    List<ProjectSummaryResponseModel> getSummaryOfProjectsOfUser(String email);

    ProjectResponseModel updateTeamLead(UpdateTeamLeadRequestDto updateTeamLeadRequestDto);

    ProjectResponseModel updateManager(UpdateManagerRequestDto requestDto);

    ProjectResponseModel updateName(UpdateNameRequestDto requestDto);

    ProjectResponseModel updateDescription(UpdateDescriptionRequestDto requestDto);

    ProjectResponseModel updateStartDate(UpdateStartDateRequestDto requestDto);

    ProjectResponseModel updateEndDate(UpdateEndDateRequestDto requestDto);

    ProjectResponseModel updateSprintDuration(UpdateSprintDurationRequestDto requestDto);

    ProjectResponseModel addMembers(AddMembersRequestDto requestDto);

    ProjectResponseModel removeMembers(RemoveMembersRequestDto requestDto);

//    void startNextSprint(UUID projectId);
//
//    void endCurrentSprint(UUID projectId);

    void deleteSprint(UUID projectId, int sprintNumber);

    void logWork(LogWorkModel logWorkModel);

    List<String> getProjectIdsOfUser(String email);
}
