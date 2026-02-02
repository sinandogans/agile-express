package com.obss.jcp.sinandogan.agileexpress.infrastructure.search.entity.models;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "search_tasks")
@AllArgsConstructor
@NoArgsConstructor
public class TaskSearchDocument {
    @Id
    private String id;
    private String taskId;
    private String projectId;
    private String name;
    private String description;
    private String assignedUserEmail;
    private String assignedUserName;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
