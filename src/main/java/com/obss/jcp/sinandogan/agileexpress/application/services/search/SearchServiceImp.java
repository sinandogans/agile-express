package com.obss.jcp.sinandogan.agileexpress.application.services.search;

import com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces.ProjectSearchRepository;
import com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces.TaskSearchRepository;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.ProjectService;
import com.obss.jcp.sinandogan.agileexpress.application.services.search.models.ProjectSearch;
import com.obss.jcp.sinandogan.agileexpress.application.services.search.models.TaskSearch;
import com.obss.jcp.sinandogan.agileexpress.application.services.search.models.requestresponse.ProjectSearchModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.search.models.requestresponse.SearchResponseModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.search.models.requestresponse.TaskSearchModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.user.UserService;
import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project.Project;
import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project.Task;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SearchServiceImp implements SearchService {
    private final ProjectSearchRepository projectSearchRepository;
    private final TaskSearchRepository taskSearchRepository;
    private final ProjectService projectService;
    private final UserService userService;


    public SearchServiceImp(ProjectSearchRepository projectSearchRepository, TaskSearchRepository taskSearchRepository, ProjectService projectService, UserService userService) {
        this.projectSearchRepository = projectSearchRepository;
        this.taskSearchRepository = taskSearchRepository;
        this.projectService = projectService;
        this.userService = userService;
    }

    public List<ProjectSearch> searchProjects(String query, String email) {
        List<String> authorizedProjectIds = projectService.getProjectIdsOfUser(email);
        return projectSearchRepository.searchByQueryAndProjectIds(query, authorizedProjectIds);
    }

    public List<TaskSearch> searchTasks(String query, String email) {
        List<String> authorizedProjectIds = projectService.getProjectIdsOfUser(email);
        return taskSearchRepository.searchByQueryAndProjectIds(query, authorizedProjectIds);
    }

    @Override
    public SearchResponseModel search(String query, String email) {
        var response = new SearchResponseModel();
        var projects = searchProjects(query, email);
        var tasks = searchTasks(query, email);
        projects.forEach(projectSearchDocument -> response.getProjects().add(new ProjectSearchModel(projectSearchDocument.getName(), projectSearchDocument.getId())));
        tasks.forEach(taskSearchDocument -> response.getTasks().add(new TaskSearchModel(taskSearchDocument.getName(), taskSearchDocument.getId(), taskSearchDocument.getProjectId())));
        return response;
    }

    @Override
    @Transactional
    public void updateProjectSearch(UUID projectId) {
        ProjectSearch projectSearch = mapToProjectSearch(projectService.getProjectById(projectId));
        projectSearchRepository.save(projectSearch);
    }

    @Override
    @Transactional
    public void deleteProjectSearch(UUID projectId) {
        projectSearchRepository.deleteById(projectId.toString());
    }

    @Override
    @Transactional
    public void updateTaskSearch(UUID taskId) {
        TaskSearch taskSearch = mapToTaskSearch(projectService.getTaskById(taskId));
        taskSearchRepository.save(taskSearch);
    }

    @Override
    @Transactional
    public void deleteTaskSearch(UUID taskId) {
        taskSearchRepository.deleteById(taskId.toString());
    }

    private ProjectSearch mapToProjectSearch(Project project) {
        ProjectSearch projectSearch = new ProjectSearch();
        projectSearch.setId(project.getId().toString());
        projectSearch.setProjectId(project.getId().toString());
        projectSearch.setName(project.getName());
        projectSearch.setDescription(project.getDescription());
        return projectSearch;
    }

    private TaskSearch mapToTaskSearch(Task task) {
        String assignedUserEmail = task.getEmailOfAssignedUser();
        String assignedUserName = null;
        if (assignedUserEmail != null)
            assignedUserName = userService.findUserByEmail(assignedUserEmail).getFirstName() + " " + userService.findUserByEmail(assignedUserEmail).getLastName();
        TaskSearch taskSearch = new TaskSearch();
        taskSearch.setTaskId(task.getId().toString());
        taskSearch.setId(task.getId().toString());
        taskSearch.setName(task.getName());
        taskSearch.setDescription(task.getDescription());
        taskSearch.setAssignedUserEmail(assignedUserEmail);
        taskSearch.setAssignedUserName(assignedUserName);
        var projectId = projectService.getProjectByBacklogId(task.getBacklogId()).getId();
        taskSearch.setProjectId(projectId.toString());
        return taskSearch;
    }
}
