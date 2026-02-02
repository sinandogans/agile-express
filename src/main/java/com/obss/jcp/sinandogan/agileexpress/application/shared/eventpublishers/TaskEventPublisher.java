package com.obss.jcp.sinandogan.agileexpress.application.shared.eventpublishers;

import java.util.UUID;

public interface TaskEventPublisher {
    void publishTaskCreated(UUID taskId);

    void publishTaskUpdated(UUID taskId);

    void publishTaskDeleted(UUID taskId);
}
