package com.codestates.back.domain.answer.AnswerRepository;

import com.codestates.back.domain.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
