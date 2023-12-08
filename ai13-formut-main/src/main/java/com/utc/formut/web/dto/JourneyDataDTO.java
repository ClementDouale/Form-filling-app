package com.utc.formut.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class JourneyDataDTO {
    @JsonProperty("questions")
    private List<JourneyEntryDataDTO> questions;
    private float score = 0;
    private int duration = 0;

    public JourneyDataDTO(List<JourneyEntryDataDTO> questions, float score, int duration) {
        this.questions = questions;
        this.score = score;
        this.duration = duration;
    }

    public List<JourneyEntryDataDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<JourneyEntryDataDTO> questions) {
        this.questions = questions;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
