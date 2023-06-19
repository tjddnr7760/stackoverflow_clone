package com.codestates.back.domain.question.infrastructure;

import com.codestates.back.domain.question.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
