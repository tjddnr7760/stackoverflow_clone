package com.codestates.back.domain.question.application;

import com.codestates.back.domain.question.controller.dto.QuestionAnswersDto;
import com.codestates.back.domain.question.controller.dto.QuestionDto;
import com.codestates.back.domain.question.infrastructure.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceV1 implements QuestionService{
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceV1(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


    @Override
    public List<QuestionDto> findAllQuestions() {
        return null;
    }

    @Override
    public QuestionDto save(QuestionDto postQuestionDto) {
        return null;
    }

    @Override
    public QuestionAnswersDto findQuestionAnswers(long questionsId) {
        return null;
    }

    @Override
    public QuestionDto updateQuestion(QuestionDto questionDto) {
        return null;
    }

    @Override
    public void deleteById(long questionId) {

    }
}
