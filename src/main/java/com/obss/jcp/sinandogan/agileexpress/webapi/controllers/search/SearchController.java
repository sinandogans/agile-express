package com.obss.jcp.sinandogan.agileexpress.webapi.controllers.search;

import com.obss.jcp.sinandogan.agileexpress.application.services.search.SearchService;
import com.obss.jcp.sinandogan.agileexpress.application.services.search.dtos.SearchResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    private final SearchService searchService;
    private final ModelMapper modelMapper;

    public SearchController(SearchService searchService, ModelMapper modelMapper) {
        this.searchService = searchService;
        this.modelMapper = modelMapper;
    }

//    @GetMapping("/api/search/projects")
//    public List<ProjectSearchDocument> searchProjects(@RequestParam String q) {
//        return searchService.searchProjects(q);
//    }
//
//    @GetMapping("/api/search/tasks")
//    public List<TaskSearchDocument> searchTasks(@RequestParam String q) {
//        return searchService.searchTasks(q);}

    @GetMapping("/api/search")
    public ResponseEntity<SearchResponseDto> search(@RequestParam String query, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(modelMapper.map(searchService.search(query, userDetails.getUsername()), SearchResponseDto.class));
    }
}


