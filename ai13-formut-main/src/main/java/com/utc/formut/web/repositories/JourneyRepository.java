package com.utc.formut.web.repositories;

import com.utc.formut.web.models.Form;
import com.utc.formut.web.models.User;
import com.utc.formut.web.models.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JourneyRepository extends JpaRepository<Journey,Long> {
    List<Journey> findAllByUser(User user);

    Optional<Journey> findByUserAndForm(User userId, Form formId);
}
