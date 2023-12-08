package com.utc.formut.web.services;

import com.utc.formut.configuration.errors.Error;
import com.utc.formut.configuration.security.JwtResponseModel;
import com.utc.formut.web.dto.*;
import com.utc.formut.web.models.*;
import com.utc.formut.web.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class FormService {

    @Autowired
    private final FormRepository formRepository;

    @Autowired
    private final QuestionRepository questionRepository;

    @Autowired
    private final AnswerRepository answerRepository;

    @Autowired
    private final UserRepository userRepository;

    ModelMapper modelMapper = new ModelMapper();

    public FormService(FormRepository formRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, UserRepository userRepository){
        this.formRepository = formRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> getAllForms(){

        List<Form> forms = this.formRepository.findAll();
        List<FormDTO> formsDTO = new ArrayList<>();
        if(forms.isEmpty()) return (ResponseEntity<?>) ResponseEntity.noContent();
        for(Form form: forms){
            formsDTO.add(modelMapper.map(form, FormDTO.class));
        }

        return ResponseEntity.ok(formsDTO);
    }

    public Form getForm(Long id) throws Exception {

        return this.formRepository.findById(id).
            orElseThrow(Exception::new);

    }

    public FormDTO createForm(FormCreationDTO formDTO){
        Form form = new Form();
        form.setVersion(formDTO.getVersion());
        if(userRepository.findById(formDTO.getWriterId()).isEmpty()) form.setWriter(userRepository.findById(formDTO.getWriterId()).get());
        else form.setWriter(userRepository.findById(formDTO.getWriterId()).get());
        form.setSubject(formDTO.getSubject());
        form.setStatus(formDTO.getStatus());

        Form resultForm = formRepository.save(form);

        return modelMapper.map(resultForm, FormDTO.class);
    }

    public void updateForm(FormDTO formDTO, Long id) throws Exception {

        Form old_form = this.formRepository.findById(id).
                orElseThrow(Exception::new);

        old_form.setSubject(formDTO.getSubject());
        old_form.setStatus(formDTO.getStatus());
        old_form.setVersion(formDTO.getVersion());
        old_form.setWriter(((User) this.userRepository.findById(formDTO.getWriterId()).orElseThrow(Exception::new)));

        this.formRepository.save(old_form);
    }

    public void deleteForm(Long id) throws Exception {
        this.formRepository.findById(id)
                .orElseThrow(Exception::new);
        this.formRepository.deleteById(id);
    }

    public Object getDataOfAForm(Long formId) {
        List<FormDataDTO> formDataArray = new ArrayList<>();
        Optional<Form> form = this.formRepository.findById(formId);

        if(form.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Error("Form not found."));

        List<Question> questionList = this.questionRepository.findQuestionsByForm(form.get());

        if(questionList.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Error("No question found for this form."));
        else if(!this.checkQuestionsStatusOfFom(questionList)) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Error("No question with status enabled found for this form."));


        for (Question question: questionList) {
            if(!Objects.equals(question.getStatus(), "enabled")) continue;
            List<Answer> answersList = this.answerRepository.findAnswersByQuestion(question);
            List<AnswerDTO> answersDTOList = new ArrayList<>();

            for (Answer answer: answersList) {
                if(!Objects.equals(answer.getStatus(), "enabled")) continue;
                answersDTOList.add(this.modelMapper.map(answer, AnswerDTO.class));
            }

            FormDataDTO formQuestionData = new FormDataDTO(this.modelMapper.map(question, QuestionDTO.class), answersDTOList);
            formDataArray.add(formQuestionData);
        }

        return formDataArray;
    }

    public boolean checkQuestionsStatusOfFom(List<Question> questionList) {
        for (Question question : questionList) {
            if (Objects.equals(question.getStatus(), "enabled")) return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }
}
