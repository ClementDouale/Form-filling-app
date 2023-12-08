package com.utc.formut.web.dto;

public class SubjectStatsDTO {

    private int journeysCount;
    private int formsCount;
    private float average;
    private float max;
    private float min;

    public SubjectStatsDTO() {
    }

    public int getJourneysCount() {
        return journeysCount;
    }

    public void setJourneysCount(int journeysCount) {
        this.journeysCount = journeysCount;
    }

    public int getFormsCount() {
        return formsCount;
    }

    public void setFormsCount(int formsCount) {
        this.formsCount = formsCount;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }


}
