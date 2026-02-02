package com.obss.jcp.sinandogan.agileexpress.application.services.project;

import com.obss.jcp.sinandogan.agileexpress.application.aop.auth.RolesAllowed;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.exceptions.UserNotFoundException;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.addmembers.AddMembersRequestDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.create.CreateProjectRequestDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.create.CreateProjectResponseDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.removemembers.RemoveMembersRequestDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.update.*;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.exceptions.*;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.models.shared.SprintResponseModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.models.summary.ProjectSummaryResponseModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.sprint.models.start.StartNextSprintModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.logwork.LogWorkModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.user.UserService;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.models.shared.ProjectResponseModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.models.shared.TaskResponseModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.create.CreateTaskModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.movetosprint.MoveTaskToSprintModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.update.UpdateTaskModel;
import com.obss.jcp.sinandogan.agileexpress.application.shared.eventpublishers.ProjectEventPublisher;
import com.obss.jcp.sinandogan.agileexpress.application.shared.eventpublishers.TaskEventPublisher;
import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project.Project;
import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project.Sprint;
import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project.Task;
import com.obss.jcp.sinandogan.agileexpress.domain.enums.Role;
import com.obss.jcp.sinandogan.agileexpress.domain.enums.StoryPoint;
import com.obss.jcp.sinandogan.agileexpress.domain.enums.TaskStatus;
import com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces.ProjectRepository;
import com.obss.jcp.sinandogan.agileexpress.domain.pojo.User;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProjectServiceImp implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final TaskEventPublisher taskEventPublisher;
    private final ProjectEventPublisher projectEventPublisher;
    private final CacheManager cacheManager;

    public ProjectServiceImp(ProjectRepository projectRepository, UserService userService, ModelMapper modelMapper, TaskEventPublisher taskEventPublisher, ProjectEventPublisher projectEventPublisher, CacheManager cacheManager) {
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.taskEventPublisher = taskEventPublisher;
        this.projectEventPublisher = projectEventPublisher;
        this.cacheManager = cacheManager;
    }

    @Override
    @Transactional
    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER})
    public CreateProjectResponseDto createProject(CreateProjectRequestDto createProjectRequestDto) {
        var projectBuilder = new Project.Builder(createProjectRequestDto.getName(), createProjectRequestDto.getEmailOfManager(), createProjectRequestDto.getSprintDurationInWeeks());
        if (createProjectRequestDto.getDescription() != null)
            projectBuilder.description(createProjectRequestDto.getDescription());
        if (createProjectRequestDto.getStartDate() != null)
            projectBuilder.startDate(createProjectRequestDto.getStartDate());
        if (createProjectRequestDto.getEndDate() != null)
            projectBuilder.endDate(createProjectRequestDto.getEndDate());
        if (createProjectRequestDto.getEmailOfTeamLead() != null) {
            checkIfUserIsTeamLeadOfAnyProject(createProjectRequestDto.getEmailOfTeamLead());
            projectBuilder.emailOfTeamLead(createProjectRequestDto.getEmailOfTeamLead());
        }
        if (createProjectRequestDto.getEmailsOfMembers() != null) {
            createProjectRequestDto.getEmailsOfMembers().forEach(this::checkIfUserIsAMemberOfAnyProject);
            projectBuilder.emailsOfMembers(createProjectRequestDto.getEmailsOfMembers());
        }
        Project newProject = projectBuilder.build();

        Project savedProject = projectRepository.save(newProject);
        projectEventPublisher.publishProjectCreated(savedProject.getId());

        return modelMapper.map(savedProject, CreateProjectResponseDto.class);
    }

    private void checkIfUserIsAMemberOfAnyProject(String emailOfUser) {
        var projects = projectRepository.findByEmailsOfMembersContaining(emailOfUser);
        if (!projects.isEmpty()) {
            throw new ProjectMemberCannotBeAssignedToTwoProjectsException("Project members cannot be assigned to more than one project.", emailOfUser);
        }
    }

    private void checkIfUserIsTeamLeadOfAnyProject(String emailOfUser) {
        var projects = projectRepository.findByEmailOfTeamLead(emailOfUser);
        if (!projects.isEmpty()) {
            throw new TeamLeadCannotBeAssignedToTwoProjectsException("Team Lead cannot be assigned to more than one project.", emailOfUser);
        }
    }

    @Override
    @Transactional
    @RolesAllowed(values = {Role.ADMIN})
    @CacheEvict(value = "projects", key = "#projectId")
    public void deleteProject(UUID projectId) {
        Project project = getById(projectId);
        projectRepository.deleteById(project.getId());
        projectEventPublisher.publishProjectDeleted(projectId);
    }

    @Transactional
    @Override
    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER})
    public ProjectResponseModel updateTeamLead(UpdateTeamLeadRequestDto requestDto) {
        Project project = getProjectById(requestDto.getProjectId());
        checkIfUserIsTeamLead(requestDto.getEmailOfTeamLead());
        checkIfUserIsTeamLeadOfAnyProject(requestDto.getEmailOfTeamLead());
        project.updateTeamLead(requestDto.getEmailOfTeamLead());
        projectRepository.save(project);
        projectEventPublisher.publishProjectUpdated(project.getId());
        return modelMapper.map(project, ProjectResponseModel.class);
    }

    private void checkIfUserIsTeamLead(String email) {
        User user = userService.findUserByEmail(email);
        if (!user.getTitle().equals("team_lead")) {
            throw new UserIsNotATeamLeadException("User is not a team lead.", email);
        }
    }

    @Transactional
    @Override
    @RolesAllowed(values = {Role.ADMIN})
    public ProjectResponseModel updateManager(UpdateManagerRequestDto requestDto) {
        Project project = getProjectById(requestDto.getProjectId());
        checkIfUserIsManager(requestDto.getEmailOfManager());
        checkIfUserIsTeamLeadOfAnyProject(requestDto.getEmailOfManager());
        project.updateTeamLead(requestDto.getEmailOfManager());
        projectRepository.save(project);
        projectEventPublisher.publishProjectUpdated(project.getId());
        return modelMapper.map(project, ProjectResponseModel.class);
    }

    private void checkIfUserIsManager(String email) {
        User user = userService.findUserByEmail(email);
        if (!user.getTitle().equals("manager")) {
            throw new UserIsNotAManagerException("User is not a manager.", email);
        }
    }

    @Transactional
    @Override
    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER})
    public ProjectResponseModel updateName(UpdateNameRequestDto requestDto) {
        Project project = getProjectById(requestDto.getProjectId());
        project.updateName(requestDto.getName());
        projectRepository.save(project);
        projectEventPublisher.publishProjectUpdated(project.getId());
        return modelMapper.map(project, ProjectResponseModel.class);
    }

    @Transactional
    @Override
    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER})
    public ProjectResponseModel updateDescription(UpdateDescriptionRequestDto requestDto) {
        Project project = getProjectById(requestDto.getProjectId());
        project.updateDescription(requestDto.getDescription());
        projectRepository.save(project);
        projectEventPublisher.publishProjectUpdated(project.getId());
        return modelMapper.map(project, ProjectResponseModel.class);
    }

    @Override
    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER})
    public ProjectResponseModel updateStartDate(UpdateStartDateRequestDto requestDto) {
        Project project = getProjectById(requestDto.getProjectId());
        if (project.getStartDate().isBefore(LocalDate.now())) {
            throw new StartDateOfStartedProjectCannotBeChangedException("Start date of started project cannot be changed.", project.getId(), project.getStartDate());
        }
        if (project.getEndDate() != null && requestDto.getStartDate().isAfter(project.getEndDate())) {
            throw new StartDateCannotBeAfterEndDateException("Start date cannot be after the end date.", requestDto.getStartDate(), project.getEndDate());
        }
        project.updateStartDate(requestDto.getStartDate());
        projectRepository.save(project);
        return modelMapper.map(project, ProjectResponseModel.class);
    }

    @Override
    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER})
    public ProjectResponseModel updateEndDate(UpdateEndDateRequestDto requestDto) {
        Project project = getProjectById(requestDto.getProjectId());
        if (project.getEndDate() != null && project.getEndDate().isBefore(LocalDate.now())) {
            throw new EndDateOfFinishedProjectCannotBeChangedException("End date of already finished project cannot be changed.", project.getId(), project.getEndDate());
        }
        if (project.getStartDate() != null && requestDto.getEndDate().isBefore(project.getStartDate())) {
            throw new EndDateCannotBeBeforeStartDateException("End date cannot be before the start date of the project.", requestDto.getEndDate(), project.getStartDate());
        }
        project.updateEndDate(requestDto.getEndDate());
        projectRepository.save(project);
        return modelMapper.map(project, ProjectResponseModel.class);
    }

    @Override
    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER})
    public ProjectResponseModel updateSprintDuration(UpdateSprintDurationRequestDto requestDto) {
        Project project = getProjectById(requestDto.getProjectId());
        if (project.getSprintDurationInWeeks() == requestDto.getSprintDurationInWeeks())
            return modelMapper.map(project, ProjectResponseModel.class);

        project.updateSprintDuration(requestDto.getSprintDurationInWeeks());
        projectRepository.save(project);
        return modelMapper.map(project, ProjectResponseModel.class);
    }

    @Override
    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER})
    public ProjectResponseModel addMembers(AddMembersRequestDto requestDto) {
        requestDto.getEmails().forEach(this::validateUser);
        requestDto.getEmails().forEach(this::checkIfUserIsAMemberOfAnyProject);
        Project project = getById(requestDto.getProjectId());
        requestDto.getEmails().forEach(project::addMember);
        projectRepository.save(project);
        return modelMapper.map(project, ProjectResponseModel.class);
    }

    @Override
    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER})
    public ProjectResponseModel removeMembers(RemoveMembersRequestDto requestDto) {
        Project project = getById(requestDto.getProjectId());
        requestDto.getEmails().forEach(project::deleteMember);
        projectRepository.save(project);
        return modelMapper.map(project, ProjectResponseModel.class);
    }

    @Override
    @Transactional
    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER, Role.TEAM_LEAD})
    @CacheEvict(value = "projects", key = "#createTaskModel.projectId")
    public TaskResponseModel createTask(CreateTaskModel createTaskModel) {
        Project project = getById(createTaskModel.getProjectId());
        Task task = project.addTask(createTaskModel.getName(), createTaskModel.getDescription(), createTaskModel.getEmailOfCreator(), null, null, createTaskModel.getEstimatedDuration());
        projectRepository.save(project);
        this.taskEventPublisher.publishTaskCreated(task.getId());
        return modelMapper.map(task, TaskResponseModel.class);
    }


    @Transactional
    @Override
    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER, Role.TEAM_LEAD})
    public void deleteTask(UUID taskId) {
        var project = getProjectByTaskId(taskId);
        project.deleteTask(taskId);
        projectRepository.save(project);
        this.taskEventPublisher.publishTaskDeleted(taskId);
//        Objects.requireNonNull(cacheManager.getCache("projects")).evict(project.getId());
    }

    @Override
    public TaskResponseModel updateTask(UpdateTaskModel updateTaskModel) {
        Project project = getProjectByTaskId(updateTaskModel.getTaskId());

        updateTaskName(project, updateTaskModel.getTaskId(), updateTaskModel.getName());

        updateTaskDescription(project, updateTaskModel.getTaskId(), updateTaskModel.getDescription());

        updateTaskStoryPoint(project, updateTaskModel.getTaskId(), updateTaskModel.getStoryPoint());

        updateTaskAssignedUser(project, updateTaskModel.getTaskId(), updateTaskModel.getEmailOfAssignedUser());

        updateTaskEstimatedDuration(project, updateTaskModel.getTaskId(), updateTaskModel.getEstimatedDuration());

        updateTaskStatus(project, updateTaskModel.getTaskId(), updateTaskModel.getStatus(), updateTaskModel.getEmail());

        Project savedProject = projectRepository.save(project);
        this.taskEventPublisher.publishTaskUpdated(updateTaskModel.getTaskId());

        //??
        Objects.requireNonNull(cacheManager.getCache("projects")).evict(project.getId());

        return modelMapper.map(savedProject.getTaskById(updateTaskModel.getTaskId()), TaskResponseModel.class);
    }

    @Override
    @Transactional
    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER, Role.TEAM_LEAD})
    @CacheEvict(value = "projects", key = "#projectId")
    public SprintResponseModel createSprint(UUID projectId) {
        Project project = getById(projectId);
        Sprint sprint = project.createSprint();
        projectRepository.save(project);
        return modelMapper.map(sprint, SprintResponseModel.class);
    }

//    @Override
//    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER, Role.TEAM_LEAD})
//    @CacheEvict(value = "projects", key = "#projectId")
//    public void startNextSprint(UUID projectId) {
//        Project project = getById(projectId);
//        project.startNextSprint();
//        projectRepository.save(project);
//    }
//
//    @Override
//    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER, Role.TEAM_LEAD})
//    @CacheEvict(value = "projects", key = "#projectId")
//    public void endCurrentSprint(UUID projectId) {
//        var project = getById(projectId);
//        project.endCurrentSprint();
//        projectRepository.save(project);
//    }

    @Override
    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER, Role.TEAM_LEAD})
    @CacheEvict(value = "projects", key = "#projectId")
    public void deleteSprint(UUID projectId, int sprintNumber) {
        Project project = getById(projectId);
        project.deleteSprint(sprintNumber);
        projectRepository.save(project);
    }

    @Override
    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER, Role.TEAM_LEAD, Role.MEMBER})
    public void logWork(LogWorkModel logWorkModel) {
        String title = userService.findUserByEmail(logWorkModel.getEmail()).getTitle();
        Project project = getProjectByTaskId(logWorkModel.getTaskId());
        if (Role.fromValue(title) == Role.MEMBER && !Objects.equals(project.getTaskById(logWorkModel.getTaskId()).getEmailOfAssignedUser(), logWorkModel.getEmail())) {
            throw new LogWorkToUnassignedTaskException("You can log work only for assigned task.", project.getId(), logWorkModel.getTaskId());
        }
        project.logWork(logWorkModel.getTaskId(), logWorkModel.getDate(), logWorkModel.getHours());
        projectRepository.save(project);
//        Objects.requireNonNull(cacheManager.getCache("projects")).evict(project.getId());
    }

    @Override
    public List<String> getProjectIdsOfUser(String email) {
        var projects = getProjectsOfUserByTitle(email);
        return projects.stream().map(project -> project.getId().toString()).toList();
    }

    @Override
//    @Cacheable(value = "projects", key = "#projectId")
    public ProjectResponseModel getProjectOfUserByProjectId(UUID projectId, String email) {
        Project project = getById(projectId);
        checkIfUserInProject(project, email);
        return modelMapper.map(project, ProjectResponseModel.class);
    }

    @Override
    public Project getById(UUID projectId) {
        var optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            return optionalProject.get();
        }
        throw new ProjectNotFoundException("Project not found");
    }

    private void checkIfUserInProject(Project project, String email) {
        if (userService.findUserByEmail(email).getTitle().equals("admin")) {
            return;
        }
        boolean isUserInProject = false;
        if (project.getEmailsOfMembers().contains(email)) {
            isUserInProject = true;
        }
        if (Objects.equals(project.getEmailOfTeamLead(), email)) {
            isUserInProject = true;
        }
        if (Objects.equals(project.getEmailOfManager(), email)) {
            isUserInProject = true;
        }
        if (!isUserInProject) {
            throw new ProjectAccessDeniedException("Access denied to project.", project.getId(), email);
        }
    }

    @Override
    public Project getProjectByBacklogId(UUID backlogId) {
        var optionalProject = projectRepository.findByBacklogId(backlogId);
        if (optionalProject.isPresent()) {
            return optionalProject.get();
        }
        throw new RuntimeException("Project with backlog id " + backlogId + " not found. Please check backlog id and try again.");
    }

    @Override
    public Project getProjectBySprintId(UUID sprintId) {
        var optionalProject = projectRepository.findBySprintsId(sprintId);
        if (optionalProject.isPresent()) {
            return optionalProject.get();
        }
        throw new RuntimeException("Project with sprint id " + sprintId + " not found. Please check sprint id and try again.");
    }

    @Override
    public List<ProjectResponseModel> getProjectsOfUser(String email) {
        List<Project> projects = getProjectsOfUserByTitle(email);
        return projects.stream()
                .map(project -> modelMapper.map(project, ProjectResponseModel.class))
                .toList();
    }

    @Override
    public List<ProjectSummaryResponseModel> getSummaryOfProjectsOfUser(String email) {
        List<Project> projects = getProjectsOfUserByTitle(email);
        return projects.stream()
                .map(project -> modelMapper.map(project, ProjectSummaryResponseModel.class))
                .toList();
    }

    private List<Project> getProjectsOfUserByTitle(String email) {
        String title = userService.findUserByEmail(email).getTitle();
        return switch (title.toLowerCase()) {
            case "admin" -> projectRepository.findAll();
            case "manager" -> projectRepository.findByEmailOfManager(email);
            case "team_lead" -> projectRepository.findByEmailOfTeamLead(email);
            default -> projectRepository.findByEmailsOfMembersContaining(email);
        };
    }


    @Override
    public Project getProjectById(UUID projectId) {
        var optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            return optionalProject.get();
        }
        throw new RuntimeException("Project not found");
    }

    @Override
    public Task getTaskById(UUID taskId) {
        var optionalProject = projectRepository.findByBacklogTasksId(taskId);
        if (optionalProject.isPresent())
            return optionalProject.get().getBacklog().getTasks().stream().filter(task -> task.getId().equals(taskId)).findFirst().orElseThrow();
        throw new RuntimeException("Task not found");
    }

//    @Override
//    public AddMembersResponseModel addMembers(AddMembersModel addMembersModel) {
//        Project project = getById(addMembersModel.getProjectId());
//        var emails = addMembersModel.getEmails();
//        emails.forEach(email -> {
//            validateMember(email);
//            project.addMember(email);
//        });
//        projectRepository.save(ProjectMapper.toEntity(project));
//        return modelMapper.map(project, AddMembersResponseModel.class);
//    }
//
//    @Override
//    public DeleteMembersResponseModel deleteMembers(DeleteMembersModel deleteMembersModel) {
//        Project project = getById(deleteMembersModel.getProjectId());
//        var emails = deleteMembersModel.getEmails();
//        emails.forEach(email -> {
//            validateMember(email);
//            project.deleteMember(email);
//        });
//        projectRepository.save(ProjectMapper.toEntity(project));
//        return modelMapper.map(project, DeleteMembersResponseModel.class);
//    }
//
//    @Override
//    public AddTeamLeadResponseModel addTeamLead(AddTeamLeadModel addTeamLeadModel) {
//        Project project = getById(addTeamLeadModel.getProjectId());
//        validateMember(addTeamLeadModel.getEmailOfTeamLead());
//        project.updateTeamLead(addTeamLeadModel.getEmailOfTeamLead());
//        projectRepository.save(ProjectMapper.toEntity(project));
//        return modelMapper.map(project, AddTeamLeadResponseModel.class);
//    }

    @Override
    @Transactional
    public TaskResponseModel moveTaskToSprint(UUID taskId, UUID sprintId) {
        var project = getProjectByTaskId(taskId);
        project.moveTaskToSprint(taskId, sprintId);
        projectRepository.save(project);
        return modelMapper.map(project.getTaskById(taskId), TaskResponseModel.class);
//        Objects.requireNonNull(cacheManager.getCache("projects")).evict(project.getId());
    }

    @Override
    public TaskResponseModel removeTaskFromSprint(UUID taskId) {
        var project = getProjectByTaskId(taskId);
        project.removeTaskFromSprint(taskId);
        var savedProject = projectRepository.save(project);
        Objects.requireNonNull(cacheManager.getCache("projects")).evict(project.getId());
        return modelMapper.map(savedProject.getTaskById(taskId), TaskResponseModel.class);
    }

    public Project getProjectByTaskId(UUID taskId) {
        var optionalProject = projectRepository.findByBacklogTasksId(taskId);
        if (optionalProject.isPresent()) {
            return optionalProject.get();
        }
        throw new RuntimeException("Project with task id " + taskId + " not found. Please check task id and try again.");
    }

    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER, Role.TEAM_LEAD})
    private void updateTaskName(Project project, UUID taskId, String newName) {
        if (newName != null && !newName.isEmpty())
            project.updateTaskName(taskId, newName);
    }

    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER, Role.TEAM_LEAD})
    private void updateTaskDescription(Project project, UUID taskId, String newDescription) {
        if (newDescription != null && !newDescription.isEmpty())
            project.updateTaskDescription(taskId, newDescription);
    }

    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER, Role.TEAM_LEAD})
    private void updateTaskStoryPoint(Project project, UUID taskId, StoryPoint storyPoint) {
        if (storyPoint != null)
            project.updateTaskStoryPoint(taskId, storyPoint);
    }

    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER, Role.TEAM_LEAD})
    private void updateTaskAssignedUser(Project project, UUID taskId, String emailOfAssignedUser) {
        if (emailOfAssignedUser != null && !emailOfAssignedUser.isEmpty()) {
            validateUser(emailOfAssignedUser);
        }
        if (emailOfAssignedUser != null && !emailOfAssignedUser.isEmpty()) {
            project.updateTaskAssignedUser(taskId, emailOfAssignedUser);
        } else {
            project.updateTaskAssignedUser(taskId, null);
        }
    }

    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER, Role.TEAM_LEAD})
    private void updateTaskEstimatedDuration(Project project, UUID taskId, int estimatedDuration) {
        if (estimatedDuration < 1) {
            throw new IllegalArgumentException("The specified estimated duration cannot be less than 1.");
        }
        project.updateTaskEstimatedDuration(taskId, estimatedDuration);
    }

    @RolesAllowed(values = {Role.ADMIN, Role.MANAGER, Role.TEAM_LEAD, Role.MEMBER})
    private void updateTaskStatus(Project project, UUID taskId, TaskStatus status, String email) {
        String title = userService.findUserByEmail(email).getTitle();
        if (Role.fromValue(title) == Role.MEMBER) {
            if (!Objects.equals(project.getTaskById(taskId).getEmailOfAssignedUser(), email)) {
                throw new RuntimeException("You are not allowed to update the status of this task.");
            }
        }
        if (status == null) {
            throw new IllegalArgumentException("The specified status cannot be null.");
        }
        project.updateTaskStatus(taskId, status);
    }

    private void validateUser(String email) {
        boolean emailExist = userService.emailExists(email);
        if (!emailExist) {
            throw new UserNotFoundException("User not found.", email);
        }
    }

}
