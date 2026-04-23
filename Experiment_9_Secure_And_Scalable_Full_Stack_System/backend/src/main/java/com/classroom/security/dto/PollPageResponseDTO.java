package com.classroom.security.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PollPageResponseDTO {

    private List<PollSummaryResponseDTO> items;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private String mode;
    private LocalDateTime nextCursorCreatedAt;
    private Long nextCursorId;

    public List<PollSummaryResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<PollSummaryResponseDTO> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public LocalDateTime getNextCursorCreatedAt() {
        return nextCursorCreatedAt;
    }

    public void setNextCursorCreatedAt(LocalDateTime nextCursorCreatedAt) {
        this.nextCursorCreatedAt = nextCursorCreatedAt;
    }

    public Long getNextCursorId() {
        return nextCursorId;
    }

    public void setNextCursorId(Long nextCursorId) {
        this.nextCursorId = nextCursorId;
    }
}
