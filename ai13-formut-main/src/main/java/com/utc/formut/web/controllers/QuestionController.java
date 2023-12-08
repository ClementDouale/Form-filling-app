package com.utc.formut.web.controllers;

import com.utc.formut.web.dto.AnswerDTO;
import com.utc.formut.web.dto.QuestionDTO;
import com.utc.formut.web.models.Answer;
import com.utc.formut.web.models.Form;
import com.utc.formut.web.models.Question;
import com.utc.formut.web.services.AnswerService;
import com.utc.formut.web.services.FormService;
import com.utc.formut.web.services.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "Form")
@RestController
@RequestMapping("/form/")
@Component
public class QuestionController {

    @Autowired
    private final QuestionService questionService;

    @Autowired
    private final FormService formService;

    @Autowired
    final AnswerService answerService;

    public QuestionController(QuestionService questionService, FormService formService, AnswerService answerService) {
        this.questionService = questionService;
        this.formService = formService;
        this.answerService = answerService;
    }

    ModelMapper modelMapper = new ModelMapper();

    @Operation(summary = "Get all questions in a form")
    @GetMapping(value = "/{form_id}/question",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<QuestionDTO> getAllQuestionsInAForm(@PathVariable("form_id") Long form_id) throws Exception {

        Form form = this.formService.getForm(form_id);

        return this.questionService.getAllQuestionsByFormId(form);
    }

    @Operation(summary = "Get all questions in a form")
    @GetMapping(value = "/{form_id}/question/{question_id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public QuestionDTO getAQuestionInAForm(@PathVariable("form_id") Long form_id,
                                           @PathVariable("question_id") Long question_id) throws Exception {

        Form form = this.formService.getForm(form_id);
        Question question = this.questionService.getQuestion(question_id);

        if (question.getForm() == form) {
            return modelMapper.map(question, QuestionDTO.class);
        } else {
            return null;
        }
    }

    @Operation(summary = "Add a question to form", description = "Add a new question in a form")
    @PostMapping(value = "/{form_id}/question/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public QuestionDTO addQuestionForm(@RequestBody @NotBlank QuestionDTO questionDTO,
                                       @PathVariable("form_id") Long id) throws Exception {

        Form form = this.formService.getForm(id);

        return this.questionService.addQuestion(modelMapper.map(questionDTO, Question.class), form);

    }

    @Operation(summary = "Update a question", description = "Update a given question from a form")
    @PutMapping(value = "/{form_id}/question/{question_id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateQuestionForm(@RequestBody @NotBlank QuestionDTO newQuestionDTO,
                                   @PathVariable("form_id") Long form_id,
                                   @PathVariable("question_id") Long question_id) throws Exception {

        Form form = this.formService.getForm(form_id);
        Question question = this.questionService.getQuestion(question_id);

        if (question.getForm() == form) {
            this.questionService.updateQuestion(newQuestionDTO, question_id);
        }
    }

    @Operation(summary = "Update a question", description = "Update a given question from a form")
    @PutMapping(value = "/{form_id}/question/{question_id}/{good_answer_id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void setQuestionAnswer(@PathVariable("form_id") Long form_id,
                                  @PathVariable("question_id") Long question_id,
                                  @PathVariable("good_answer_id") Long good_answer_id) throws Exception {

        Form form = this.formService.getForm(form_id);
        Question question = this.questionService.getQuestion(question_id);
        Answer good_answer = this.answerService.getAnswer(good_answer_id);

        if (question.getForm() == form) {
            this.questionService.setQuestionAnswer(question_id, good_answer);
        }
    }

    @Operation(summary = "delete question", description = "delete question")
    @DeleteMapping(value = "/{form_id}/question/{question_id}/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteQuestionForm(@PathVariable("form_id") Long form_id,
                                          @PathVariable("question_id") Long question_id) throws Exception {

        Form form = this.formService.getForm(form_id);
        Question question = this.questionService.getQuestion(question_id);

        if (question.getForm() == form) {
            this.questionService.deleteQuestion(question_id);
        }
    }
}
