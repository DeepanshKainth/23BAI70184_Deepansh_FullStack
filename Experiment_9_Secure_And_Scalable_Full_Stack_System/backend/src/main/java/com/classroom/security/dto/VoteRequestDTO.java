package com.classroom.security.dto;

import jakarta.validation.constraints.NotNull;

public class VoteRequestDTO {

    @NotNull(message = "Option id is required")
    private Long optionId;

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }
}