package com.classroom.security.repository;

import com.classroom.security.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    boolean existsByPollIdAndVoterName(Long pollId, String voterName);
}