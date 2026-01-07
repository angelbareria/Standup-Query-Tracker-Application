package com.querytracker.standup_query_tracker.service;

import com.querytracker.standup_query_tracker.dto.QueryRequest;
import com.querytracker.standup_query_tracker.exception.ResourceNotFoundException;
import com.querytracker.standup_query_tracker.model.*;
import com.querytracker.standup_query_tracker.repository.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author abareria
 **/

@Service
public class QueryService {
    @Autowired
    public QueryRepository queryRepository;

    @Autowired
    public UserService userService;

    @Autowired
    public SLAService slaService;

    //create new query
    public StandupQuery createQuery(QueryRequest request){
        User raisedBy = userService.getUserById(request.getRaisedById());

        StandupQuery query = new StandupQuery();
        query.setTitle(request.getTitle());
        query.setDescription(request.getDescription());
        query.setPriority(request.getPriority());
        query.setType(request.getType());
        query.setRaisedBy(raisedBy);
        query.setStatus(Status.OPEN);

        LocalDateTime slaDeadline = slaService.calculateSLADeadline(request.getPriority(),LocalDateTime.now());
        query.setSlaDeadline(slaDeadline);
        return queryRepository.save(query);
    }

    //Get query by ID
    public StandupQuery getQueryById(Long id){
        return queryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Query","id",id));
    }

    // get all queries
    public List<StandupQuery> getAllQueries(){
        return queryRepository.findAllOrderByCreatedDateDesc();
    }

    //Assign query to a user
    public StandupQuery assignQuery(Long queryId, Long userId){
        StandupQuery query = getQueryById(queryId);
        User assignedUser = userService.getUserById(userId);

        query.setAssignedTo(assignedUser);

        if (query.getStatus() == Status.OPEN){
            query.setStatus(Status.IN_PROGRESS);
        }

        return queryRepository.save(query);
    }

    //update query status
    public StandupQuery updateStatus(Long queryId, Status status){
        StandupQuery query = getQueryById(queryId);
        query.setStatus(status);

        // if status becomes resolved or closed , the resolution time is calculated and resolved date is set
        if(status == Status.RESOLVED || status == Status.CLOSED){
            LocalDateTime resolvedDate = LocalDateTime.now();
            query.setResolvedDate(resolvedDate);

            Long resolutionTime = slaService.calculateResoltionTime(query.getCreatedDate(),resolvedDate);
            query.setResolutionTimeHours(resolutionTime);
        }
        return queryRepository.save(query);
    }

    //Get queries by status
    public List<StandupQuery> getQueriesByStatus(Status status){
        return queryRepository.findByStatus(status);
    }

    //get queries by type
    public List<StandupQuery> getQueriesByType(QueryType type){
        return queryRepository.findByType(type);
    }

    //Get queries raised by a user
    public List<StandupQuery> getQueriesRaisedByUser(Long userId){
        User user = userService.getUserById(userId);
        return queryRepository.findByRaisedBy(user);
    }

    //Get queries assigned to user
    public List<StandupQuery> getQueriesAssignedToUser(Long userId){
        User user = userService.getUserById(userId);
        return queryRepository.findByAssignedTo(user);
    }

    //Gte overdue queries
    public List<StandupQuery> getOverdueQueries(){
        return queryRepository.findOverdueQueries(LocalDateTime.now());
    }

    //Search queries by title - Left
    public List<StandupQuery> searchQueriesByTitle(String keyword){
        return queryRepository.findAll().stream().filter(query -> query.getTitle().toLowerCase().contains(keyword.toLowerCase())).toList();
    }

    //Delete query
    public void deleteQuery(Long queryId){
        StandupQuery query = getQueryById(queryId);
        queryRepository.delete(query);
    }

    //Update query details
    public StandupQuery updateQuery(Long queryId, QueryRequest request){
        StandupQuery query = getQueryById(queryId);

        query.setTitle(request.getTitle());
        query.setDescription(request.getDescription());
        query.setType(request.getType());
        query.setPriority(request.getPriority());

        //recalculate SLA if priority changed
        LocalDateTime newSlaDeadline = slaService.calculateSLADeadline(request.getPriority(),query.getCreatedDate());
        query.setSlaDeadline(newSlaDeadline);

        return queryRepository.save(query);

    }

}
