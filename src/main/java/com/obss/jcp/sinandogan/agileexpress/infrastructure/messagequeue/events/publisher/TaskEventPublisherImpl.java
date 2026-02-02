package com.obss.jcp.sinandogan.agileexpress.infrastructure.messagequeue.events.publisher;


import com.obss.jcp.sinandogan.agileexpress.infrastructure.config.messagequeue.RabbitMqConfig;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.messagequeue.events.TaskEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Service
public class TaskEventPublisherImpl implements com.obss.jcp.sinandogan.agileexpress.application.shared.eventpublishers.TaskEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public TaskEventPublisherImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishTaskCreated(UUID taskId) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.TASK_EXCHANGE, RabbitMqConfig.TASK_ROUTING_KEY, new TaskEvent("CREATE", taskId));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishTaskUpdated(UUID taskId) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.TASK_EXCHANGE, RabbitMqConfig.TASK_ROUTING_KEY, new TaskEvent("UPDATE", taskId));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishTaskDeleted(UUID taskId) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.TASK_EXCHANGE, RabbitMqConfig.TASK_ROUTING_KEY, new TaskEvent("DELETE", taskId));
    }
}
