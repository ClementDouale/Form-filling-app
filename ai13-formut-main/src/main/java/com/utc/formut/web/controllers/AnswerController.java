package com.utc.formut.web.controllers;

import com.utc.formut.web.dto.AnswerDTO;
import com.utc.formut.web.dto.FormDTO;
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
@RequestMapping("/form")
@Component
public class AnswerController {

    @Autowired
    private final AnswerService answerService;

    @Autowired
    private final FormService formService;

    @Autowired
    private final QuestionService questionService;

    public AnswerController(AnswerService answerService, FormService formService, QuestionService questionService){
        this.answerService = answerService;
        this.formService = formService;
        this.questionService = questionService;
    }

    ModelMapper modelMapper = new ModelMapper();

    @Operation(summary = "Get correct answer from question", description = "Find the correct answer from a given question")
    @GetMapping(value  = "/{form_id}/question/{question_id}/correctAnswer",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AnswerDTO getCorrectAnswer(@PathVariable("form_id") Long  form_id,
                                      @PathVariable("question_id") Long question_id) throws Exception {

        Form form = this.formService.getForm(form_id);
        Question question = this.questionService.getQuestion(question_id);

        if(question.getForm() == form){
            return modelMapper.map(question.getGood_answer(), AnswerDTO.class);
        }
        else{
            return null;
        }

    }

    @Operation(summary = "Get an answer", description = "Find an answer from a given question")
    @GetMapping(value  = "/{form_id}/question/{question_id}/answer/{answer_id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AnswerDTO getAnswer(@PathVariable("form_id") Long  form_id,
                               @PathVariable("question_id") Long question_id,
                               @PathVariable("answer_id") Long answer_id) throws Exception {

        Form form = this.formService.getForm(form_id);
        Question question = this.questionService.getQuestion(question_id);
        Answer answer = this.answerService.getAnswer(answer_id);

        if(question.getForm() == form){
            if(answer.getQuestion() == question){
                return modelMapper.map(answer, AnswerDTO.class);
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }

    @Operation(summary = "Get all answers from a question", description = "Get all answers from a question")
    @GetMapping(value = "/{form_id}/question/{question_id}/answer",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<AnswerDTO> getAllAnswersFromAQuestion(@PathVariable("form_id") Long form_id,
                                                      @PathVariable("question_id") Long question_id) throws Exception {

        Form form = this.formService.getForm(form_id);
        Question question = this.questionService.getQuestion(question_id);

        if(question.getForm() == form){
            return this.answerService.getAllAnswersByQuestionId(question);
        }
        else{
            return null;
        }
    }

    @Operation(summary = "Add a new answer  to a question", description = "Add a new answer choice to a question in a form")
    @PostMapping(value  = "/{form_id}/question/{question_id}/answer",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AnswerDTO addAnswer(@RequestBody @NotBlank AnswerDTO answerDTO,
                               @PathVariable("form_id") Long form_id,
                               @PathVariable("question_id") Long question_id) throws Exception {

        Form form = this.formService.getForm(form_id);
        Question question = this.questionService.getQuestion(question_id);

        if(question.getForm() == form){
            return this.answerService.addAnswer(modelMapper.map(answerDTO, Answer.class), question);
        }
        else{
            return null;
        }
    }

    @Operation(summary = "Update an answer from a question", description = "Add a new answer choice to a question in a form")
    @PutMapping(value  = "/{form_id}/question/{question_id}/answer/{answer_id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateAnswer(@RequestBody @NotBlank AnswerDTO newAnswerDTO,
                             @PathVariable("form_id") Long form_id,
                             @PathVariable("question_id") Long question_id,
                             @PathVariable("answer_id") Long answer_id) throws Exception {

        Form form = this.formService.getForm(form_id);
        Question question = this.questionService.getQuestion(question_id);
        Answer answer = this.answerService.getAnswer(answer_id);

        if(question.getForm() == form) {
            if (answer.getQuestion() == question) {
                this.answerService.updateAnswer(newAnswerDTO, answer_id);
            }
        }
    }

    @Operation(summary = "Delete an answer", description = "Delete a question's answer, if it was the correct answer, the question will be deleted")
    @DeleteMapping(value  = "/{form_id}/question/{question_id}/answer/{answer_id}/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteAnswer(@PathVariable("form_id") Long form_id,
                                  @PathVariable("question_id") Long question_id,
                                  @PathVariable("answer_id") Long answer_id) throws Exception {

        Form form = this.formService.getForm(form_id);
        Question question = this.questionService.getQuestion(question_id);
        Answer answer = this.answerService.getAnswer(answer_id);

        if(question.getForm() == form) {
            if (answer.getQuestion() == question) {
                this.answerService.deleteAnswer(answer_id);
            }
        }
    }
}
