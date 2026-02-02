package com.obss.jcp.sinandogan.agileexpress.application.email;

import com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces.EmailTaskRepository;
import com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces.ProjectRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class StartupEmailCheck {

    private final EmailTaskRepository emailTaskRepository;
    private final EmailService emailService;
    private final ProjectRepository projectRepository;

    public StartupEmailCheck(EmailTaskRepository emailTaskRepository, EmailService emailService, ProjectRepository projectRepository) {
        this.emailTaskRepository = emailTaskRepository;
        this.emailService = emailService;
        this.projectRepository = projectRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void checkAndSendMissedEmails() {
        LocalDate today = LocalDate.now();
        var optionalEmailTask = emailTaskRepository.findByDate(today);
        if (optionalEmailTask.isEmpty())
            return;
        var emailTask = optionalEmailTask.get();
        if (emailTask.isSent())
            return;
        var projects = projectRepository.findActiveProjectsWithSprints(today);
        projects.forEach(projectEntity -> {
            projectEntity.getSprints().forEach(sprintEntity -> {
                if (sprintEntity.getEndDate() == today) {
                    emailService.sendEmail(projectEntity.getEmailOfManager(), "Sprint End Reminder", "Sprint " + sprintEntity.getSprintNumber() + " of project " + projectEntity.getName() + " ends today.");
                }
            });
        });
        emailTask.setDate(today);
        emailTask.setSent(true);
        emailTaskRepository.save(emailTask);
    }
}

