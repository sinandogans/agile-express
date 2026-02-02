package com.obss.jcp.sinandogan.agileexpress.application.services.search.models.requestresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponseModel {
    List<ProjectSearchModel> projects = new ArrayList<>();
    List<TaskSearchModel> tasks = new ArrayList<>();
}
