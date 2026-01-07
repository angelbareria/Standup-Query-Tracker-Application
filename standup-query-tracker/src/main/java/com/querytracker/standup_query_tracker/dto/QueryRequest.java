package com.querytracker.standup_query_tracker.dto;

import com.querytracker.standup_query_tracker.model.Priority;
import com.querytracker.standup_query_tracker.model.QueryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author abareria
 **/
@Data
public class QueryRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title should have less than 200 characters")
    private String title;

    @Size(max = 1000, message = "Description should be under 1000 characters")
    private String description;

    @NotNull(message = "Type is required")
    private QueryType type;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotNull(message = "User Id raising the query is required")
    private Long raisedById;

}
