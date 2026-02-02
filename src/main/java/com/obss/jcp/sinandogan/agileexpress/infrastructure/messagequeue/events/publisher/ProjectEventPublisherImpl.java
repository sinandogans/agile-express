package com.obss.jcp.sinandogan.agileexpress.infrastructure.messagequeue.events.publisher;

import com.obss.jcp.sinandogan.agileexpress.application.shared.eventpublishers.ProjectEventPublisher;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.config.messagequeue.RabbitMqConfig;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.messagequeue.events.ProjectEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Service
public class ProjectEventPublisherImpl implements ProjectEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ProjectEventPublisherImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishProjectCreated(UUID projectId) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.PROJECT_EXCHANGE, RabbitMqConfig.PROJECT_ROUTING_KEY, new ProjectEvent("CREATE", projectId));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishProjectUpdated(UUID projectId) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.PROJECT_EXCHANGE, RabbitMqConfig.PROJECT_ROUTING_KEY, new ProjectEvent("UPDATE", projectId));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishProjectDeleted(UUID projectId) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.PROJECT_EXCHANGE, RabbitMqConfig.PROJECT_ROUTING_KEY, new ProjectEvent("DELETE", projectId));
    }
}

