package com.querytracker.standup_query_tracker.contoller;

import com.querytracker.standup_query_tracker.dto.AssignRequest;
import com.querytracker.standup_query_tracker.dto.QueryRequest;
import com.querytracker.standup_query_tracker.dto.UpdateStatusRequest;
import com.querytracker.standup_query_tracker.model.QueryType;
import com.querytracker.standup_query_tracker.model.StandupQuery;
import com.querytracker.standup_query_tracker.model.Status;
import com.querytracker.standup_query_tracker.service.QueryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.geom.RectangularShape;
import java.util.List;

/**
 * @author abareria
 **/

@RestController
@RequestMapping("/api/queries")
@CrossOrigin(origins = "*")
public class QueryController {

    @Autowired
    private QueryService queryService;

    //Create new query
    @PostMapping
    public ResponseEntity<StandupQuery> createQuery(@Valid @RequestBody QueryRequest request){
        StandupQuery query = queryService.createQuery(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(query);
    }

    //Get all queries
    @GetMapping
    public ResponseEntity<List<StandupQuery>> getAllQueries(){
        List<StandupQuery> queries = queryService.getAllQueries();
        return ResponseEntity.ok(queries);
    }

    //get query by id
    @GetMapping("/{id}")
    public ResponseEntity<StandupQuery> getQueryById(@PathVariable Long id){
        StandupQuery query = queryService.getQueryById(id);
        return ResponseEntity.ok(query);
    }

    //update query details
    @PutMapping("/{id}")
    public ResponseEntity<StandupQuery> updateQuery(@PathVariable Long id, @Valid @RequestBody QueryRequest request){
        StandupQuery updatedQuery = queryService.updateQuery(id, request);
        return ResponseEntity.ok(updatedQuery);
    }

    //assign query to user
    @PutMapping("/{id}/assign")
    public ResponseEntity<StandupQuery> assignQuery(@PathVariable Long id, @Valid @RequestBody AssignRequest request){
        StandupQuery assignedQuery = queryService.assignQuery(id, request.getUserId());
        return ResponseEntity.ok(assignedQuery);
    }

    //update query status
    @PutMapping("/{id}/status")
    public ResponseEntity<StandupQuery> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest request){
        StandupQuery query = queryService.updateStatus(id,request.getStatus());
        return ResponseEntity.ok(query);
    }

    //get query by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<StandupQuery>> getQueryByStatus(@PathVariable Status status){
        List<StandupQuery> queries = queryService.getQueriesByStatus(status);
        return ResponseEntity.ok(queries);
    }

    //get query by type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<StandupQuery>> getQueryByType(@PathVariable QueryType type){
        List<StandupQuery> queries = queryService.getQueriesByType(type);
        return ResponseEntity.ok(queries);
    }

    //get queries raised by user
    @GetMapping("/raised-by/{userId}")
    public ResponseEntity<List<StandupQuery>> getQueryRaisedByUser(@PathVariable Long userId){
        List<StandupQuery> queries = queryService.getQueriesRaisedByUser(userId);
        return ResponseEntity.ok(queries);
    }

    //get queries assigned to user
    @GetMapping("/assigned-to/{userId}")
    public ResponseEntity<List<StandupQuery>> getQueryAssignedToUser(@PathVariable Long userId){
        List<StandupQuery> queries = queryService.getQueriesAssignedToUser(userId);
        return ResponseEntity.ok(queries);
    }

    // get overdue queries
    @GetMapping("/overdue")
    public ResponseEntity<List<StandupQuery>> getOverdueQueries(){
        List<StandupQuery> queries = queryService.getOverdueQueries();
        return ResponseEntity.ok(queries);
    }

    //search queries by title
    @GetMapping("/search")
    public ResponseEntity<List<StandupQuery>> searchQueries(@RequestParam String keyword) {
        List<StandupQuery> queries = queryService.searchQueriesByTitle(keyword);
        return ResponseEntity.ok(queries);
    }

    //delete query
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuery(@PathVariable Long id) {
        queryService.deleteQuery(id);
        return ResponseEntity.noContent().build();
    }
}
