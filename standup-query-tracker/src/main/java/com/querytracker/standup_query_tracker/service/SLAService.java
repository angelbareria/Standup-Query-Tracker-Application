package com.querytracker.standup_query_tracker.service;
import com.querytracker.standup_query_tracker.model.Priority;
import org.springframework.stereotype.Service;
import com.querytracker.standup_query_tracker.model.StandupQuery;
import java.time.LocalDateTime;
import java.time.Duration;

/**
 * @author abareria
 **/

@Service
public class SLAService {

    //SLA hours based on priority
    public static final int HIGH_PRIORITY_SLA_HOURS = 24;
    public static final int MEDIUM_PRIORITY_SLA_HOURS = 72;
    public static final int LOW_PRIORITY_SLA_HOURS = 168;

    //calculate SLA deadline
    public LocalDateTime calculateSLADeadline(Priority priority, LocalDateTime createdDate){
        long slahours = switch (priority){
            case HIGH -> HIGH_PRIORITY_SLA_HOURS;
            case MEDIUM -> MEDIUM_PRIORITY_SLA_HOURS;
            case LOW -> LOW_PRIORITY_SLA_HOURS;
        };
        return createdDate.plusHours(slahours);
    }

    //check overdue query
    public boolean isOverdue(StandupQuery query){
        if(query.getSlaDeadline()==null){
            return false;
        }
        return (LocalDateTime.now().isAfter(query.getSlaDeadline()) && !query.getStatus().toString().equals("RESOLVED") && !query.getStatus().toString().equals("CLOSED"));
    }

    //Calculate resolution time in hours
    public Long calculateResoltionTime(LocalDateTime createdDate, LocalDateTime resolvedDate){
        if(createdDate==null || resolvedDate==null){
            return null;
        }

        Duration duration = Duration.between(createdDate,resolvedDate);
        return duration.toHours();
    }

    //get SLA Status Indicator
    public String getSLAStatus(StandupQuery query){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = query.getSlaDeadline();

        if(deadline == null){
            return "No_SLA";
        }

        if(query.getStatus().toString().equals("RESOLVED")){
            return "COMPLETED";
        }

        if(isOverdue(query)){
            return "OVERDUE";
        }

        Duration remainingTime = Duration.between(now,deadline);
        long remainingHours = remainingTime.toHours();

        if(remainingHours<4){
            return "CRITICAL";
        }
        else if(remainingHours < 12){
            return "WARNING";
        }
        else{
            return "ON_TRACK";
        }
    }

    //Get hours remaining until sla breach
    public Long getHoursRemaining(StandupQuery query){
        if(query.getSlaDeadline()==null || query.getStatus().toString().equals("RESOLVED") || query.getStatus().toString().equals("CLOSED")){
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        Duration timeRemaining = Duration.between(now,query.getSlaDeadline());
        return timeRemaining.toHours();
    }

}
