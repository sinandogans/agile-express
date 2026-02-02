package com.obss.jcp.sinandogan.agileexpress.application;

import com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces.ProjectRepository;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.ProjectServiceImp;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.create.CreateProjectRequestDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.create.CreateProjectResponseDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.exceptions.ProjectAccessDeniedException;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.exceptions.ProjectMemberCannotBeAssignedToTwoProjectsException;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.exceptions.ProjectNotFoundException;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.exceptions.TeamLeadCannotBeAssignedToTwoProjectsException;
import com.obss.jcp.sinandogan.agileexpress.application.services.project.models.shared.ProjectResponseModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.user.UserService;
import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project.Project;
import com.obss.jcp.sinandogan.agileexpress.domain.pojo.User;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.messagequeue.events.publisher.ProjectEventPublisherImpl;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.messagequeue.events.publisher.TaskEventPublisherImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private TaskEventPublisherImpl taskEventPublisher;

    @Mock
    private ProjectEventPublisherImpl projectEventPublisherImpl;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private ProjectServiceImp projectService;

    private Project testProject;
    private UUID testProjectId;
    private String managerEmail;
    private String teamLeadEmail;
    private String memberEmail;

    @BeforeEach
    void setUp() {
        testProjectId = UUID.randomUUID();
        managerEmail = "manager@test.com";
        teamLeadEmail = "teamlead@test.com";
        memberEmail = "member@test.com";

        testProject = new Project.Builder("Test Project", managerEmail, 2)
                .emailOfTeamLead(teamLeadEmail)
                .emailsOfMembers(List.of(memberEmail))
                .description("Test Description")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(3))
                .build();
    }

    private User createUser(String email, String title) {
        return new User(null, "testuser", email, "Test", "User", title);
    }

    @Test
    void getProjectOfUserByProjectId_WithValidUser_ShouldReturnProject() {
        // Arrange
        User user = createUser(memberEmail, "member");
        ProjectResponseModel expectedResponse = new ProjectResponseModel();

        when(projectRepository.findById(testProjectId)).thenReturn(Optional.of(testProject));
        when(userService.findUserByEmail(memberEmail)).thenReturn(user);
        when(modelMapper.map(testProject, ProjectResponseModel.class)).thenReturn(expectedResponse);

        // Act
        ProjectResponseModel result = projectService.getProjectOfUserByProjectId(testProjectId, memberEmail);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(projectRepository).findById(testProjectId);
        verify(userService).findUserByEmail(memberEmail);
    }

    @Test
    void getProjectOfUserByProjectId_WithUnauthorizedUser_ShouldThrowException() {
        // Arrange
        String unauthorizedEmail = "unauthorized@test.com";
        User user = createUser(unauthorizedEmail, "member");

        when(projectRepository.findById(testProjectId)).thenReturn(Optional.of(testProject));
        when(userService.findUserByEmail(unauthorizedEmail)).thenReturn(user);

        // Act & Assert
        assertThrows(ProjectAccessDeniedException.class,
                () -> projectService.getProjectOfUserByProjectId(testProjectId, unauthorizedEmail));
    }

    @Test
    void getProjectOfUserByProjectId_WithAdminUser_ShouldReturnProject() {
        // Arrange
        String adminEmail = "admin@test.com";
        User adminUser = createUser(adminEmail, "admin");

        ProjectResponseModel expectedResponse = new ProjectResponseModel();

        when(projectRepository.findById(testProjectId)).thenReturn(Optional.of(testProject));
        when(userService.findUserByEmail(adminEmail)).thenReturn(adminUser);
        when(modelMapper.map(testProject, ProjectResponseModel.class)).thenReturn(expectedResponse);

        // Act
        ProjectResponseModel result = projectService.getProjectOfUserByProjectId(testProjectId, adminEmail);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    void getProjectOfUserByProjectId_WithNonExistentProject_ShouldThrowException() {
        // Arrange
        when(projectRepository.findById(testProjectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProjectNotFoundException.class,
                () -> projectService.getProjectOfUserByProjectId(testProjectId, memberEmail));
    }

    @Test
    void getProjectsOfUser_AsAdmin_ShouldReturnAllProjects() {
        // Arrange
        String adminEmail = "admin@test.com";
        User adminUser = createUser(adminEmail, "admin");

        ProjectResponseModel projectResponse = new ProjectResponseModel();

        when(userService.findUserByEmail(adminEmail)).thenReturn(adminUser);
        when(projectRepository.findAll()).thenReturn(List.of(testProject));
        when(modelMapper.map(testProject, ProjectResponseModel.class)).thenReturn(projectResponse);

        // Act
        List<ProjectResponseModel> result = projectService.getProjectsOfUser(adminEmail);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(projectResponse, result.get(0));
        verify(projectRepository).findAll();
        verify(userService).findUserByEmail(adminEmail);
    }

    @Test
    void getProjectsOfUser_AsManager_ShouldReturnManagedProjects() {
        // Arrange
        User managerUser = createUser(managerEmail, "manager");

        ProjectResponseModel projectResponse = new ProjectResponseModel();

        when(userService.findUserByEmail(managerEmail)).thenReturn(managerUser);
        when(projectRepository.findByEmailOfManager(managerEmail)).thenReturn(List.of(testProject));
        when(modelMapper.map(testProject, ProjectResponseModel.class)).thenReturn(projectResponse);

        // Act
        List<ProjectResponseModel> result = projectService.getProjectsOfUser(managerEmail);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectRepository).findByEmailOfManager(managerEmail);
    }

    @Test
    void getProjectsOfUser_AsTeamLead_ShouldReturnTeamLeadProjects() {
        // Arrange
        User teamLeadUser = createUser(teamLeadEmail, "team_lead");

        ProjectResponseModel projectResponse = new ProjectResponseModel();

        when(userService.findUserByEmail(teamLeadEmail)).thenReturn(teamLeadUser);
        when(projectRepository.findByEmailOfTeamLead(teamLeadEmail)).thenReturn(List.of(testProject));
        when(modelMapper.map(testProject, ProjectResponseModel.class)).thenReturn(projectResponse);

        // Act
        List<ProjectResponseModel> result = projectService.getProjectsOfUser(teamLeadEmail);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectRepository).findByEmailOfTeamLead(teamLeadEmail);
    }

    @Test
    void getProjectsOfUser_AsMember_ShouldReturnMemberProjects() {
        // Arrange
        User memberUser = createUser(memberEmail, "member");

        ProjectResponseModel projectResponse = new ProjectResponseModel();

        when(userService.findUserByEmail(memberEmail)).thenReturn(memberUser);
        when(projectRepository.findByEmailsOfMembersContaining(memberEmail)).thenReturn(List.of(testProject));
        when(modelMapper.map(testProject, ProjectResponseModel.class)).thenReturn(projectResponse);

        // Act
        List<ProjectResponseModel> result = projectService.getProjectsOfUser(memberEmail);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectRepository).findByEmailsOfMembersContaining(memberEmail);
    }

    @Test
    void getProjectsOfUser_WithMultipleProjects_ShouldReturnAllUserProjects() {
        // Arrange
        User managerUser = createUser(managerEmail, "manager");

        Project project2 = new Project.Builder("Test Project 2", managerEmail, 2).build();

        ProjectResponseModel response1 = new ProjectResponseModel();
        ProjectResponseModel response2 = new ProjectResponseModel();

        when(userService.findUserByEmail(managerEmail)).thenReturn(managerUser);
        when(projectRepository.findByEmailOfManager(managerEmail)).thenReturn(List.of(testProject, project2));
        when(modelMapper.map(testProject, ProjectResponseModel.class)).thenReturn(response1);
        when(modelMapper.map(project2, ProjectResponseModel.class)).thenReturn(response2);

        // Act
        List<ProjectResponseModel> result = projectService.getProjectsOfUser(managerEmail);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ============================================
    // CREATE PROJECT TESTS
    // ============================================

    /**
     * Test Case 1: Başarılı proje oluşturma - Tüm alanlar dolu
     * Açıklama: Manager tüm zorunlu ve opsiyonel alanları vererek proje oluşturur
     * Beklenen: Proje başarıyla kaydedilir, event publish edilir, response döner
     */
    @Test
    void createProject_WithAllFields_ShouldCreateProjectSuccessfully() {
        // Arrange
        String newTeamLeadEmail = "newteamlead@test.com";
        String newMemberEmail = "newmember@test.com";

        CreateProjectRequestDto requestDto = new CreateProjectRequestDto();
        requestDto.setName("New Project");
        requestDto.setDescription("New Description");
        requestDto.setEmailOfManager(managerEmail);
        requestDto.setEmailOfTeamLead(newTeamLeadEmail);
        requestDto.setEmailsOfMembers(List.of(newMemberEmail));
        requestDto.setStartDate(LocalDate.now());
        requestDto.setEndDate(LocalDate.now().plusMonths(3));
        requestDto.setSprintDurationInWeeks(2);

        Project savedProject = new Project.Builder("New Project", managerEmail, 2)
                .description("New Description")
                .emailOfTeamLead(newTeamLeadEmail)
                .emailsOfMembers(List.of(newMemberEmail))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(3))
                .build();

        CreateProjectResponseDto expectedResponse = new CreateProjectResponseDto();

        when(projectRepository.findByEmailOfTeamLead(newTeamLeadEmail)).thenReturn(List.of());
        when(projectRepository.findByEmailsOfMembersContaining(newMemberEmail)).thenReturn(List.of());
        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);
        when(modelMapper.map(savedProject, CreateProjectResponseDto.class)).thenReturn(expectedResponse);

        // Act
        CreateProjectResponseDto result = projectService.createProject(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(projectRepository).findByEmailOfTeamLead(newTeamLeadEmail);
        verify(projectRepository).findByEmailsOfMembersContaining(newMemberEmail);
        verify(projectRepository).save(any(Project.class));
        verify(projectEventPublisherImpl).publishProjectCreated(any());
    }

    /**
     * Test Case 2: Başarılı proje oluşturma - Sadece zorunlu alanlar
     * Açıklama: Manager sadece zorunlu alanları (name, manager email, sprint duration) vererek proje oluşturur
     * Beklenen: Proje başarıyla oluşturulur, opsiyonel alanlar null olarak kalır
     */
    @Test
    void createProject_WithOnlyRequiredFields_ShouldCreateProjectSuccessfully() {
        // Arrange
        CreateProjectRequestDto requestDto = new CreateProjectRequestDto();
        requestDto.setName("Minimal Project");
        requestDto.setEmailOfManager(managerEmail);
        requestDto.setSprintDurationInWeeks(2);

        Project savedProject = new Project.Builder("Minimal Project", managerEmail, 2).build();
        CreateProjectResponseDto expectedResponse = new CreateProjectResponseDto();

        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);
        when(modelMapper.map(savedProject, CreateProjectResponseDto.class)).thenReturn(expectedResponse);

        // Act
        CreateProjectResponseDto result = projectService.createProject(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(projectRepository).save(any(Project.class));
        verify(projectEventPublisherImpl).publishProjectCreated(any());
        // Team lead ve member kontrolü yapılmamalı çünkü null
        verify(projectRepository, never()).findByEmailOfTeamLead(any());
        verify(projectRepository, never()).findByEmailsOfMembersContaining(any());
    }

    /**
     * Test Case 3: Team Lead zaten başka bir projede
     * Açıklama: Atanmak istenen team lead zaten başka bir projenin team lead'i
     * Beklenen: TeamLeadCannotBeAssignedToTwoProjectsException fırlatılır
     */
    @Test
    void createProject_WithTeamLeadAlreadyAssigned_ShouldThrowException() {
        // Arrange
        CreateProjectRequestDto requestDto = new CreateProjectRequestDto();
        requestDto.setName("New Project");
        requestDto.setEmailOfManager(managerEmail);
        requestDto.setEmailOfTeamLead(teamLeadEmail); // Bu team lead zaten mevcut projede
        requestDto.setSprintDurationInWeeks(2);

        when(projectRepository.findByEmailOfTeamLead(teamLeadEmail)).thenReturn(List.of(testProject));

        // Act & Assert
        assertThrows(TeamLeadCannotBeAssignedToTwoProjectsException.class,
                () -> projectService.createProject(requestDto));

        verify(projectRepository).findByEmailOfTeamLead(teamLeadEmail);
        verify(projectRepository, never()).save(any(Project.class));
        verify(projectEventPublisherImpl, never()).publishProjectCreated(any());
    }

    /**
     * Test Case 4: Member zaten başka bir projede
     * Açıklama: Atanmak istenen member zaten başka bir projenin üyesi
     * Beklenen: ProjectMemberCannotBeAssignedToTwoProjectsException fırlatılır
     */
    @Test
    void createProject_WithMemberAlreadyAssigned_ShouldThrowException() {
        // Arrange
        CreateProjectRequestDto requestDto = new CreateProjectRequestDto();
        requestDto.setName("New Project");
        requestDto.setEmailOfManager(managerEmail);
        requestDto.setEmailsOfMembers(List.of(memberEmail)); // Bu member zaten mevcut projede
        requestDto.setSprintDurationInWeeks(2);

        when(projectRepository.findByEmailsOfMembersContaining(memberEmail)).thenReturn(List.of(testProject));

        // Act & Assert
        assertThrows(ProjectMemberCannotBeAssignedToTwoProjectsException.class,
                () -> projectService.createProject(requestDto));

        verify(projectRepository).findByEmailsOfMembersContaining(memberEmail);
        verify(projectRepository, never()).save(any(Project.class));
        verify(projectEventPublisherImpl, never()).publishProjectCreated(any());
    }

    /**
     * Test Case 5: Birden fazla member, birisi zaten atanmış
     * Açıklama: Birden fazla member eklenirken, listeden biri zaten başka bir projede
     * Beklenen: ProjectMemberCannotBeAssignedToTwoProjectsException fırlatılır
     */
    @Test
    void createProject_WithMultipleMembersOneAlreadyAssigned_ShouldThrowException() {
        // Arrange
        String newMember1 = "newmember1@test.com";
        String newMember2 = "newmember2@test.com";

        CreateProjectRequestDto requestDto = new CreateProjectRequestDto();
        requestDto.setName("New Project");
        requestDto.setEmailOfManager(managerEmail);
        requestDto.setEmailsOfMembers(List.of(newMember1, memberEmail, newMember2)); // memberEmail zaten atanmış
        requestDto.setSprintDurationInWeeks(2);

        when(projectRepository.findByEmailsOfMembersContaining(newMember1)).thenReturn(List.of());
        when(projectRepository.findByEmailsOfMembersContaining(memberEmail)).thenReturn(List.of(testProject));

        // Act & Assert
        assertThrows(ProjectMemberCannotBeAssignedToTwoProjectsException.class,
                () -> projectService.createProject(requestDto));

        verify(projectRepository, never()).save(any(Project.class));
        verify(projectEventPublisherImpl, never()).publishProjectCreated(any());
    }

    /**
     * Test Case 6: Team Lead ve Member kontrolü - İkisi de yeni kullanıcı
     * Açıklama: Hem team lead hem de member listesi verilir, hiçbiri başka projede değil
     * Beklenen: Proje başarıyla oluşturulur
     */
    @Test
    void createProject_WithNewTeamLeadAndMembers_ShouldCreateSuccessfully() {
        // Arrange
        String newTeamLead = "newteamlead@test.com";
        String newMember1 = "newmember1@test.com";
        String newMember2 = "newmember2@test.com";

        CreateProjectRequestDto requestDto = new CreateProjectRequestDto();
        requestDto.setName("New Project");
        requestDto.setEmailOfManager(managerEmail);
        requestDto.setEmailOfTeamLead(newTeamLead);
        requestDto.setEmailsOfMembers(List.of(newMember1, newMember2));
        requestDto.setSprintDurationInWeeks(2);

        Project savedProject = new Project.Builder("New Project", managerEmail, 2)
                .emailOfTeamLead(newTeamLead)
                .emailsOfMembers(List.of(newMember1, newMember2))
                .build();

        CreateProjectResponseDto expectedResponse = new CreateProjectResponseDto();

        when(projectRepository.findByEmailOfTeamLead(newTeamLead)).thenReturn(List.of());
        when(projectRepository.findByEmailsOfMembersContaining(newMember1)).thenReturn(List.of());
        when(projectRepository.findByEmailsOfMembersContaining(newMember2)).thenReturn(List.of());
        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);
        when(modelMapper.map(savedProject, CreateProjectResponseDto.class)).thenReturn(expectedResponse);

        // Act
        CreateProjectResponseDto result = projectService.createProject(requestDto);

        // Assert
        assertNotNull(result);
        verify(projectRepository).findByEmailOfTeamLead(newTeamLead);
        verify(projectRepository).findByEmailsOfMembersContaining(newMember1);
        verify(projectRepository).findByEmailsOfMembersContaining(newMember2);
        verify(projectRepository).save(any(Project.class));
        verify(projectEventPublisherImpl).publishProjectCreated(any());
    }

    /**
     * Test Case 7: Event publisher çağrısı doğrulama
     * Açıklama: Proje oluşturulduğunda event publisher'ın doğru project ID ile çağrıldığını doğrula
     * Beklenen: publishProjectCreated metodu kaydedilen projenin ID'si ile çağrılır
     */
    @Test
    void createProject_ShouldPublishProjectCreatedEvent() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        CreateProjectRequestDto requestDto = new CreateProjectRequestDto();
        requestDto.setName("Event Test Project");
        requestDto.setEmailOfManager(managerEmail);
        requestDto.setSprintDurationInWeeks(2);

        Project savedProject = new Project.Builder("Event Test Project", managerEmail, 2).build();
        // Project ID'yi reflection ile set etmek yerine, mock davranışı ile ID'yi alacağız
        CreateProjectResponseDto expectedResponse = new CreateProjectResponseDto();

        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);
        when(modelMapper.map(savedProject, CreateProjectResponseDto.class)).thenReturn(expectedResponse);

        // Act
        projectService.createProject(requestDto);

        // Assert
        verify(projectEventPublisherImpl).publishProjectCreated(savedProject.getId());
    }

}
