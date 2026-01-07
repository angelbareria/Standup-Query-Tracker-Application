package com.querytracker.standup_query_tracker.repository;

import com.querytracker.standup_query_tracker.model.StandupQuery;
import com.querytracker.standup_query_tracker.model.QueryType;
import com.querytracker.standup_query_tracker.model.Status;
import com.querytracker.standup_query_tracker.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QueryRepository extends JpaRepository<StandupQuery, Long> {

    List<StandupQuery> findByStatus(Status status);

    List<StandupQuery> findByType(QueryType type);

    List<StandupQuery> findByRaisedBy(User user);

    List<StandupQuery> findByAssignedTo(User user);

    List<StandupQuery> findByStatusAndType(Status status, QueryType type);

    @Query(
            value = """
            SELECT *
            FROM queries
            WHERE sla_deadline < :now
              AND status IN ('OPEN', 'IN_PROGRESS')
        """,
            nativeQuery = true
    )
    List<StandupQuery> findOverdueQueries(@Param("now") LocalDateTime now);

    @Query(
            value = """
            SELECT *
            FROM queries
            ORDER BY created_date DESC
        """,
            nativeQuery = true
    )
    List<StandupQuery> findAllOrderByCreatedDateDesc();
}
