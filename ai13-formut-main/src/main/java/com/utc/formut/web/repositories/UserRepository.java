package com.utc.formut.web.repositories;

import com.utc.formut.web.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findById(Long id);
    List<User> findAllByMail(String mail);
    Optional<User> findByMail(String username);
    Optional<User> findByName(String username);
}