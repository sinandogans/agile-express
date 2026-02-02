package com.obss.jcp.sinandogan.agileexpress.infrastructure.search.repositories.mongo;

import com.obss.jcp.sinandogan.agileexpress.infrastructure.search.entity.models.TaskSearchDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoTaskSearchRepository extends MongoRepository<TaskSearchDocument, String> {

    @Query("{ $and: [ " +
            "  { $or: [ " +
            "      { 'name': { $regex: ?0, $options: 'i' } }, " +
            "      { 'description': { $regex: ?0, $options: 'i' } }, " +
            "      { 'assignedUserEmail': { $regex: ?0, $options: 'i' } }, " +
            "      { 'assignedUserName': { $regex: ?0, $options: 'i' } } " +
            "    ] }, " +
            "  { 'projectId': { $in: ?1 } } " +
            "]}")
    List<TaskSearchDocument> searchByQueryAndProjectIds(String query, List<String> authorizedProjectIds);
}
