package com.utc.formut.web.dto;

public class JourneyDTO {
    private Long id;
    private float score;
    private int duration;
    private Long internId;
    private Long formId;

    public JourneyDTO(Long id, float score, int duration, Long internId, Long formId) {
        this.id = id;
        this.score = score;
        this.duration = duration;
        this.internId = internId;
        this.formId = formId;
    }

    public JourneyDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getInternId() {
        return internId;
    }

    public void setInternId(Long internId) {
        this.internId = internId;
    }

    public Long getformId() {
        return formId;
    }

    public void setformId(Long formId) {
        this.formId = formId;
    }
}
