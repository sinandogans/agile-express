package com.obss.jcp.sinandogan.agileexpress.infrastructure.search.repositories.mongo;

import com.obss.jcp.sinandogan.agileexpress.infrastructure.search.entity.models.ProjectSearchDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoProjectSearchRepository extends MongoRepository<ProjectSearchDocument, String> {
    @Query("{ $and: [ " +
            "  { $or: [ " +
            "      { \"name\": { $regex: ?0, $options: \"i\" } }, " +
            "      { \"description\": { $regex: ?0, $options: \"i\" } } " +
            "    ] }, " +
            "  { \"projectId\": { $in: ?1 } } " +
            "]}")
    List<ProjectSearchDocument> searchByQueryAndProjectIds(String query, List<String> authorizedProjectIds);
}

