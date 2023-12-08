package com.utc.formut.web.dto;

public class CoupleQuestionAnswerDTO {
    private Long questionID;
    private Long answerID;

    public CoupleQuestionAnswerDTO(Long questionID, Long answerID) {
        this.questionID = questionID;
        this.answerID = answerID;
    }

    public Long getQuestionId() {
        return questionID;
    }

    public void setQuestionId(Long questionID) {
        this.questionID = questionID;
    }

    public Long getAnswerId() {
        return answerID;
    }

    public void setAnswerId(Long answerID) {
        this.answerID = answerID;
    }
}
