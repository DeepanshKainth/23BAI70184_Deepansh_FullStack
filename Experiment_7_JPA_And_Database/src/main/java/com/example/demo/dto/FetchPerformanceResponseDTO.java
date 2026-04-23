package com.example.demo.dto;

public class FetchPerformanceResponseDTO {

    private int patientCount;
    private long totalRecords;
    private long lazyFetchTimeMs;
    private long optimizedFetchTimeMs;
    private String recommendation;

    public int getPatientCount() {
        return patientCount;
    }

    public void setPatientCount(int patientCount) {
        this.patientCount = patientCount;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public long getLazyFetchTimeMs() {
        return lazyFetchTimeMs;
    }

    public void setLazyFetchTimeMs(long lazyFetchTimeMs) {
        this.lazyFetchTimeMs = lazyFetchTimeMs;
    }

    public long getOptimizedFetchTimeMs() {
        return optimizedFetchTimeMs;
    }

    public void setOptimizedFetchTimeMs(long optimizedFetchTimeMs) {
        this.optimizedFetchTimeMs = optimizedFetchTimeMs;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}