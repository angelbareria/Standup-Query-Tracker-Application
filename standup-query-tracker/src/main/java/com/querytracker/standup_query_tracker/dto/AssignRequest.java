package com.querytracker.standup_query_tracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author abareria
 **/
@Data
public class AssignRequest {

    @NotNull(message =  "User Id is required")
    private Long userId;
}
