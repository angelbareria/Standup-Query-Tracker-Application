package com.querytracker.standup_query_tracker.dto;

import com.querytracker.standup_query_tracker.model.StandupQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author abareria
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    private long totalQueries;
    private long openQueries;
    private long overdueQueries;
    private long myRaisedQueries;
    private long myAssignedQueries;
    private List<StandupQuery> recentQueries;
}
