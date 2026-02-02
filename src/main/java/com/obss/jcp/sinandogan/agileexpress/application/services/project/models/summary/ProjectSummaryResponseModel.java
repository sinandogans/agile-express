package com.obss.jcp.sinandogan.agileexpress.application.services.project.models.summary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectSummaryResponseModel {
    UUID id;
    String name;
}
