package com.classroom.security.repository;

import com.classroom.security.entity.PollStatus;

import java.time.LocalDateTime;

public interface PollSummaryView {

    Long getId();

    String getQuestion();

    String getDescription();

    PollStatus getStatus();

    String getCreatedBy();

    LocalDateTime getCreatedAt();

    LocalDateTime getExpiresAt();

    long getTotalVotes();
}
