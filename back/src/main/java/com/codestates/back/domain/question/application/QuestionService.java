package com.codestates.back.domain.question.application;

import com.codestates.back.domain.question.controller.dto.QuestionAnswersDto;
import com.codestates.back.domain.question.controller.dto.QuestionDto;

import java.util.List;

public interface QuestionService {

    List<QuestionDto> findQuestions(int page);

    long countAllQuestions();

    QuestionDto findQuestion(long questionId);

    QuestionDto save(QuestionDto postQuestionDto, long userId);

    QuestionAnswersDto findQuestionAnswers(long questionsId);

    QuestionDto updateQuestion(QuestionDto questionDto);

    void deleteById(long questionId);
}
