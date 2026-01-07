package com.querytracker.standup_query_tracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * @author abareria
 **/

@Entity
@Table(name= "queries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandupQuery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private QueryType type;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    //Ownership of queries : Foreign Key traced to user.id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "raised_by_id")
    private User raisedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;

    //Timestamps required
    @Column(name = "created_date", nullable = false, updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @Column(name = "last_updated")
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdated;

    //SLa Monitoring
    @Column(name = "sla_deadline")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime slaDeadline;

    @Column(name = "resolved_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime resolvedDate;

    //Resolution monitoring
    @Column(name = "resolution_time_hours")
    private Long resolutionTimeHours;

    // Ensuring time and status is set before the query is inserted
    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = Status.OPEN;
        }
    }

}
