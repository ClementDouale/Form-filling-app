package com.utc.formut.web.dto;

import java.util.ArrayList;

public class StatisticsUserDTO {
    private Integer journeysAmount = null;
    private Float averageScore = null;
    private Float averageDuration = null;
    private Float bestScore = null;
    private Float worstScore = null;

    public Integer getJourneysAmount() {
        return journeysAmount;
    }

    public void setJourneysAmount(Integer journeysAmount) {
        this.journeysAmount = journeysAmount;
    }

    public Float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Float averageScore) {
        this.averageScore = averageScore;
    }

    public Float getAverageDuration() {
        return averageDuration;
    }

    public void setAverageDuration(Float averageDuration) {
        this.averageDuration = averageDuration;
    }

    public Float getBestScore() {
        return bestScore;
    }

    public void setBestScore(Float bestScore) {
        this.bestScore = bestScore;
    }

    public Float getWorstScore() {
        return worstScore;
    }

    public void setWorstScore(Float worstScore) {
        this.worstScore = worstScore;
    }

    public StatisticsUserDTO(Integer journeysAmount, Float averageScore, Float averageDuration, Float bestScore, Float worstScore) {
        this.journeysAmount = journeysAmount;
        this.averageScore = averageScore;
        this.averageDuration = averageDuration;
        this.bestScore = bestScore;
        this.worstScore = worstScore;
    }

    public StatisticsUserDTO() {
    }
}
