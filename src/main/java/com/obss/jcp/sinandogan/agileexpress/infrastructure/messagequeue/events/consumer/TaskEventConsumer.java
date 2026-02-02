package com.obss.jcp.sinandogan.agileexpress.infrastructure.messagequeue.events.consumer;

import com.obss.jcp.sinandogan.agileexpress.application.services.search.SearchService;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.config.messagequeue.RabbitMqConfig;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.messagequeue.events.TaskEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class TaskEventConsumer {

    private final SearchService searchService;

    public TaskEventConsumer(SearchService searchService) {
        this.searchService = searchService;
    }

    @RabbitListener(queues = RabbitMqConfig.TASK_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void handleTaskEvent(TaskEvent event) {
        switch (event.getAction()) {
            case "CREATE":
            case "UPDATE":
                searchService.updateTaskSearch(event.getTaskId());
                break;
            case "DELETE":
                searchService.deleteTaskSearch(event.getTaskId());
                break;
            default:
        }
    }
}
