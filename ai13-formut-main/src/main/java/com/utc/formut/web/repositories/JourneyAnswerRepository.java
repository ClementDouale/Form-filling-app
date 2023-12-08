package com.utc.formut.web.repositories;

import com.utc.formut.web.models.Journey;
import com.utc.formut.web.models.JourneyAnswer;
import com.utc.formut.web.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JourneyAnswerRepository extends JpaRepository<JourneyAnswer,Long> {
    Optional<JourneyAnswer> findByJourneyAndQuestion(Journey journey, Question question);
}
