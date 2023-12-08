package com.utc.formut.web.repositories;

import com.utc.formut.web.models.Form;
import com.utc.formut.web.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {

    List<Question> findQuestionsByForm(Form form);
}
