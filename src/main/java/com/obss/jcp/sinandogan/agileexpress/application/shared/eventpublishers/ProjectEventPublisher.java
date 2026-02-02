package com.obss.jcp.sinandogan.agileexpress.application.shared.eventpublishers;

import java.util.UUID;

public interface ProjectEventPublisher {
    void publishProjectCreated(UUID projectId);

    void publishProjectUpdated(UUID projectId);

    void publishProjectDeleted(UUID projectId);
}
