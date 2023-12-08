package com.utc.formut.web.services;

import com.utc.formut.web.dto.AnswerDTO;
import com.utc.formut.web.dto.FormDTO;
import com.utc.formut.web.models.Answer;
import com.utc.formut.web.models.Form;
import com.utc.formut.web.models.Question;
import com.utc.formut.web.repositories.AnswerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AnswerService {

    @Autowired
    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository){
        this.answerRepository = answerRepository;
    }

    ModelMapper modelMapper = new ModelMapper();

    public Answer getAnswer(Long id){
        return this.answerRepository.getById(id);
    }

    public List<AnswerDTO> getAllAnswersByQuestionId(Question question){

        List<Answer> answers = this.answerRepository.findAnswersByQuestion(question);
        List<AnswerDTO> answerDTOS = new ArrayList<>();

        for(Answer answer: answers){
            answerDTOS.add(modelMapper.map(answer, AnswerDTO.class));
        }

        return answerDTOS;

    }

    public AnswerDTO addAnswer(Answer answer, Question question){
        answer.setQuestion(question);
        return modelMapper.map(this.answerRepository.save(answer), AnswerDTO.class);
    }

    public void updateAnswer(AnswerDTO answerDTO, Long old_answer_id){

        Answer old_answer = this.answerRepository.getById(old_answer_id);

        old_answer.setStatus(answerDTO.getStatus());
        old_answer.setContent(answerDTO.getContent());

        this.answerRepository.save(old_answer);

    }

    public void deleteAnswer(Long id) throws Exception {
        this.answerRepository.findById(id)
                .orElseThrow(Exception::new);
        this.answerRepository.deleteById(id);
    }

}
