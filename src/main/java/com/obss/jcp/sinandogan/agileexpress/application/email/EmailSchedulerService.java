package com.obss.jcp.sinandogan.agileexpress.application.email;

import com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces.EmailTaskRepository;
import com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces.ProjectRepository;
import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.email.EmailTask;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmailSchedulerService {

    private final EmailTaskRepository emailTaskRepository;
    private final EmailService emailService;
    private final ProjectRepository projectRepository;

    public EmailSchedulerService(EmailTaskRepository emailTaskRepository, EmailService emailService, ProjectRepository projectRepository) {
        this.emailTaskRepository = emailTaskRepository;
        this.emailService = emailService;
        this.projectRepository = projectRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void sendDailyEmail() {
        LocalDate today = LocalDate.now();
        var projects = projectRepository.findActiveProjectsWithSprints(today);
        projects.forEach(projectEntity -> {
            projectEntity.getSprints().forEach(sprintEntity -> {
                if (sprintEntity.getEndDate() == today) {
                    emailService.sendEmail(projectEntity.getEmailOfManager(), "Sprint End Reminder", "Sprint " + sprintEntity.getSprintNumber() + " of project " + projectEntity.getName() + " ends today.");
                }
            });
        });
        EmailTask emailTask = new EmailTask();
        emailTask.setDate(today);
        emailTask.setSent(true);
        emailTaskRepository.save(emailTask);
    }
}
