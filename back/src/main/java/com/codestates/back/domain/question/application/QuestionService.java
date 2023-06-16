package com.codestates.back.domain.question.application;

import com.codestates.back.domain.question.controller.dto.QuestionAnswersDto;
import com.codestates.back.domain.question.controller.dto.QuestionDto;

import java.util.List;

public interface QuestionService {

    List<QuestionDto> findAllQuestions();

    QuestionDto save(QuestionDto postQuestionDto);

    QuestionAnswersDto findQuestionAnswers(long questionsId);

    QuestionDto updateQuestion(QuestionDto questionDto);

    void deleteById(long questionId);
}
