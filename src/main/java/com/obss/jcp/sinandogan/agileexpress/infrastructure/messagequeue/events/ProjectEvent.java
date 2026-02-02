package com.obss.jcp.sinandogan.agileexpress.infrastructure.messagequeue.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEvent implements Serializable {
    private String action;
    private UUID projectId;
}
