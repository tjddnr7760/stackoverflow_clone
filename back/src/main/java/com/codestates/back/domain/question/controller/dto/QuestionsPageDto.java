package com.codestates.back.domain.question.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class QuestionsPageDto {
    long allQuestionNumber;
    List<QuestionDto> questionsList;
}
