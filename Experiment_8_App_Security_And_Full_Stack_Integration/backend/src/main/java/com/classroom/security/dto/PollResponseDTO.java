package com.classroom.security.dto;

import com.classroom.security.entity.PollStatus;

import java.time.LocalDateTime;
import java.util.List;

public class PollResponseDTO {

    private Long id;
    private String question;
    private String description;
    private PollStatus status;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private int totalVotes;
    private List<PollOptionResponseDTO> options;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PollStatus getStatus() {
        return status;
    }

    public void setStatus(PollStatus status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public List<PollOptionResponseDTO> getOptions() {
        return options;
    }

    public void setOptions(List<PollOptionResponseDTO> options) {
        this.options = options;
    }
}