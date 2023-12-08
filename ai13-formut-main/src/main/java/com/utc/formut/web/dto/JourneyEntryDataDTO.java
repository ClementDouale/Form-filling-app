package com.utc.formut.web.dto;

import java.util.List;

public class JourneyEntryDataDTO {
    private QuestionDTO question;
    private List<AnswerDTO> answers;
    private Long answer_user = 0L;
    private Long answer_good = 0L;

    public JourneyEntryDataDTO(QuestionDTO question, List<AnswerDTO> answers, Long answer_user, Long answer_good) {
        this.question = question;
        this.answers = answers;
        this.answer_user = answer_user;
        this.answer_good = answer_good;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }

    public Long getAnswer_user() {
        return answer_user;
    }

    public void setAnswer_user(Long answer_user) {
        this.answer_user = answer_user;
    }

    public Long getAnswer_good() {
        return answer_good;
    }

    public void setAnswer_good(Long answer_good) {
        this.answer_good = answer_good;
    }
}
