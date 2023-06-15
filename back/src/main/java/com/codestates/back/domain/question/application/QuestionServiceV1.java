package com.codestates.back.domain.question.application;

import com.codestates.back.domain.question.infrastructure.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceV1 implements QuestionService{
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceV1(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


}
