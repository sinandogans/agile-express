package com.obss.jcp.sinandogan.agileexpress.application.services.search.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponseDto {
    List<ProjectSearchDto> projects;
    List<TaskSearchDto> tasks;
}
