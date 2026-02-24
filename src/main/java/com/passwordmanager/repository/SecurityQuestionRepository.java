package com.passwordmanager.repository;

import com.passwordmanager.entity.SecurityQuestion;
import com.passwordmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecurityQuestionRepository
        extends JpaRepository<SecurityQuestion, Long> {

    List<SecurityQuestion> findByUser(User user);

    void deleteByUser(User user);
}
