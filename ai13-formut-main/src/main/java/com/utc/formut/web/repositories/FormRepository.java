package com.utc.formut.web.repositories;

import com.utc.formut.web.models.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Repository
public interface FormRepository extends JpaRepository<Form,Long> {
}
