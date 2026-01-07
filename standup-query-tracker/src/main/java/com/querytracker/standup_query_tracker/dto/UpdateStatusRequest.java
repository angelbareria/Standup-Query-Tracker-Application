package com.querytracker.standup_query_tracker.dto;

import com.querytracker.standup_query_tracker.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author abareria
 **/
@Data
public class UpdateStatusRequest {
    @NotNull(message = "Status is required")
    private Status status;
}
