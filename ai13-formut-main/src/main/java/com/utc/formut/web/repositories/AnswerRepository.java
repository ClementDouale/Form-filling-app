package com.utc.formut.web.repositories;

import com.utc.formut.web.models.Answer;
import com.utc.formut.web.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long> {

    List<Answer> findAnswersByQuestion(Question question);

}
