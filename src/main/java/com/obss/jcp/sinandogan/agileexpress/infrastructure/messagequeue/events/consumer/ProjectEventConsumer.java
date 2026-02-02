package com.obss.jcp.sinandogan.agileexpress.infrastructure.messagequeue.events.consumer;

import com.obss.jcp.sinandogan.agileexpress.application.services.search.SearchService;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.config.messagequeue.RabbitMqConfig;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.messagequeue.events.ProjectEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ProjectEventConsumer {

    private final SearchService searchService;

    public ProjectEventConsumer(SearchService searchService) {
        this.searchService = searchService;
    }

    @RabbitListener(queues = RabbitMqConfig.PROJECT_QUEUE)
    public void handleProjectEvent(ProjectEvent event) {
        switch (event.getAction()) {
            case "CREATE":
            case "UPDATE":
                searchService.updateProjectSearch(event.getProjectId());
                break;
            case "DELETE":
                searchService.deleteProjectSearch(event.getProjectId());
                break;
            default:
        }
    }
}
