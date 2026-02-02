package com.obss.jcp.sinandogan.agileexpress.infrastructure.search.entity.models;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Data
@Document(collection = "search_projects")
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSearchDocument {
    @Id
    private String id;
    private String projectId;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
}
