package com.utc.formut.web.services;

import com.utc.formut.web.dto.AnswerDTO;
import com.utc.formut.web.dto.QuestionDTO;
import com.utc.formut.web.models.Answer;
import com.utc.formut.web.models.Form;
import com.utc.formut.web.models.Question;
import com.utc.formut.web.repositories.QuestionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class QuestionService {

    @Autowired
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository){
        this.questionRepository = questionRepository;
    }

    ModelMapper modelMapper = new ModelMapper();

    public Question getQuestion(Long id){
        return this.questionRepository.getById(id);
    }

    public List<QuestionDTO> getAllQuestionsByFormId(Form form){

        List<Question> questions = this.questionRepository.findQuestionsByForm(form);
        List<QuestionDTO> questionDTOS = new ArrayList<>();

        for(Question question: questions){
            questionDTOS.add(modelMapper.map(question, QuestionDTO.class));
        }

        return questionDTOS;
    }

    public QuestionDTO addQuestion(Question question, Form form){
        question.setForm(form);
        return modelMapper.map(this.questionRepository.save(question), QuestionDTO.class);
    }

    public void updateQuestion(QuestionDTO questionDTO, Long old_question_id){

        Question old_question = this.questionRepository.getById(old_question_id);

        old_question.setStatus(questionDTO.getStatus());
        old_question.setWording(questionDTO.getWording());

        this.questionRepository.save(old_question);

    }

    public void setQuestionAnswer(Long old_question_id, Answer good_answer){

        Question old_question = this.questionRepository.getById(old_question_id);

        old_question.setGood_answer(good_answer);

        this.questionRepository.save(old_question);

    }

    public void deleteQuestion(Long id) throws Exception {
        this.questionRepository.findById(id)
                .orElseThrow(Exception::new);
        this.questionRepository.deleteById(id);
    }



}
