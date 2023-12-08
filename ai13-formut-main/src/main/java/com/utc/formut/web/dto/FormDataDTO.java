package com.utc.formut.web.dto;

import java.util.List;

public class FormDataDTO {
    private QuestionDTO question;
    private List<AnswerDTO> answers;

    public FormDataDTO(QuestionDTO question, List<AnswerDTO> answers) {
        this.question = question;
        this.answers = answers;
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
}
