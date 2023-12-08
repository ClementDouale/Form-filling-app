package com.utc.formut.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CreateJourneyDTO {
    @JsonProperty("answers")
    private List<CoupleQuestionAnswerDTO> answers;
    private int duration;

    public CreateJourneyDTO(List<CoupleQuestionAnswerDTO> coupleQuestionAnswerDTOList, int duration) {
        this.answers = coupleQuestionAnswerDTOList;
        this.duration = duration;
    }

    public List<CoupleQuestionAnswerDTO> getCoupleQuestionAnswerDTOList() {
        return answers;
    }

    public void setCoupleQuestionAnswerDTOList(List<CoupleQuestionAnswerDTO> coupleQuestionAnswerDTOList) {
        this.answers = coupleQuestionAnswerDTOList;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
