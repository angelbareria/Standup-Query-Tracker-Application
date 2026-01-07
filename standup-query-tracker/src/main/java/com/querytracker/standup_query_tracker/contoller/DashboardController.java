package com.querytracker.standup_query_tracker.contoller;

import com.querytracker.standup_query_tracker.dto.DashboardResponse;
import com.querytracker.standup_query_tracker.model.StandupQuery;
import com.querytracker.standup_query_tracker.model.Status;
import com.querytracker.standup_query_tracker.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author abareria
 **/
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin("*")
public class DashboardController {

    @Autowired
    private QueryService queryService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(){
        List<StandupQuery> allQueries = queryService.getAllQueries();
        List<StandupQuery> openQueries = queryService.getQueriesByStatus(Status.OPEN);
        List<StandupQuery> overdueQueries = queryService.getOverdueQueries();

        // Get recent queries (limit to 10)
        List<StandupQuery> recentQueries = allQueries.stream()
                .limit(10)
                .toList();

        DashboardResponse response = new DashboardResponse();
        response.setTotalQueries(allQueries.size());
        response.setOpenQueries(openQueries.size());
        response.setOverdueQueries(overdueQueries.size());
        response.setMyRaisedQueries(0); // Will be set by user-specific endpoint
        response.setMyAssignedQueries(0); // Will be set by user-specific endpoint
        response.setRecentQueries(recentQueries);

        return ResponseEntity.ok(response);
    }

    /**
     * Get user-specific dashboard
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<DashboardResponse> getUserDashboard(@PathVariable Long userId) {
        List<StandupQuery> allQueries = queryService.getAllQueries();
        List<StandupQuery> openQueries = queryService.getQueriesByStatus(Status.OPEN);
        List<StandupQuery> overdueQueries = queryService.getOverdueQueries();
        List<StandupQuery> myRaisedQueries = queryService.getQueriesRaisedByUser(userId);
        List<StandupQuery> myAssignedQueries = queryService.getQueriesAssignedToUser(userId);

        // Get recent queries (limit to 10)
        List<StandupQuery> recentQueries = allQueries.stream()
                .limit(10)
                .toList();

        DashboardResponse response = new DashboardResponse();
        response.setTotalQueries(allQueries.size());
        response.setOpenQueries(openQueries.size());
        response.setOverdueQueries(overdueQueries.size());
        response.setMyRaisedQueries(myRaisedQueries.size());
        response.setMyAssignedQueries(myAssignedQueries.size());
        response.setRecentQueries(recentQueries);

        return ResponseEntity.ok(response);
    }

    /**
     * Get statistics summary
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getStatistics() {
        List<StandupQuery> allQueries = queryService.getAllQueries();

        long totalQueries = allQueries.size();
        long openCount = allQueries.stream().filter(q -> q.getStatus() == Status.OPEN).count();
        long inProgressCount = allQueries.stream().filter(q -> q.getStatus() == Status.IN_PROGRESS).count();
        long resolvedCount = allQueries.stream().filter(q -> q.getStatus() == Status.RESOLVED).count();
        long closedCount = allQueries.stream().filter(q -> q.getStatus() == Status.CLOSED).count();
        long overdueCount = queryService.getOverdueQueries().size();

        return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
            put("totalQueries", totalQueries);
            put("openQueries", openCount);
            put("inProgressQueries", inProgressCount);
            put("resolvedQueries", resolvedCount);
            put("closedQueries", closedCount);
            put("overdueQueries", overdueCount);
        }});
    }
}
