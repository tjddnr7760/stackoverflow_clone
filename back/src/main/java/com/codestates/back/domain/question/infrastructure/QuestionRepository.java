package com.codestates.back.domain.question.infrastructure;

import com.codestates.back.domain.question.domain.QuestionV1;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<QuestionV1, Long> {
}
