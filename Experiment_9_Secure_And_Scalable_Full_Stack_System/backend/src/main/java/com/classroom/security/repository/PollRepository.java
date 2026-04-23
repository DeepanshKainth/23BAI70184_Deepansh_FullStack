package com.classroom.security.repository;

import com.classroom.security.entity.Poll;
import com.classroom.security.entity.PollStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {

    @EntityGraph(attributePaths = "options")
    @Query("select distinct p from Poll p order by p.createdAt desc")
    List<Poll> findAllWithOptions();

    @EntityGraph(attributePaths = "options")
    Optional<Poll> findById(Long id);

    @Query("""
        select p.id as id,
               p.question as question,
               p.description as description,
               p.status as status,
               p.createdBy as createdBy,
               p.createdAt as createdAt,
               p.expiresAt as expiresAt,
               coalesce(sum(o.voteCount), 0) as totalVotes
        from Poll p
        left join p.options o
        where (:status is null or p.status = :status)
        group by p.id, p.question, p.description, p.status, p.createdBy, p.createdAt, p.expiresAt
        order by p.createdAt desc, p.id desc
        """)
    Page<PollSummaryView> findPollSummaries(@Param("status") PollStatus status, Pageable pageable);

    @Query("""
        select p.id as id,
               p.question as question,
               p.description as description,
               p.status as status,
               p.createdBy as createdBy,
               p.createdAt as createdAt,
               p.expiresAt as expiresAt,
               coalesce(sum(o.voteCount), 0) as totalVotes
        from Poll p
        left join p.options o
        where (:status is null or p.status = :status)
          and (:cursorCreatedAt is null
               or p.createdAt < :cursorCreatedAt
               or (p.createdAt = :cursorCreatedAt and p.id < :cursorId))
        group by p.id, p.question, p.description, p.status, p.createdBy, p.createdAt, p.expiresAt
        order by p.createdAt desc, p.id desc
        """)
    List<PollSummaryView> findPollSummariesByCursor(@Param("status") PollStatus status,
                                                    @Param("cursorCreatedAt") LocalDateTime cursorCreatedAt,
                                                    @Param("cursorId") Long cursorId,
                                                    Pageable pageable);
}