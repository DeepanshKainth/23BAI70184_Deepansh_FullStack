package com.classroom.security.repository;

import com.classroom.security.entity.Poll;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {

    @EntityGraph(attributePaths = "options")
    @Query("select distinct p from Poll p order by p.createdAt desc")
    List<Poll> findAllWithOptions();

    @EntityGraph(attributePaths = "options")
    Optional<Poll> findById(Long id);
}