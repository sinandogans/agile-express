package com.obss.jcp.sinandogan.agileexpress.application.services.search.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSearch {
    private String id;
    private String projectId;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
}
