package com.codestates.back.domain.question.controller.dto;

import com.codestates.back.domain.answer.dto.AnswerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class QuestionAnswersDto {
    private Long id;
    private String title;
    private String body;
    private Date createdAt;
    List<AnswerDto> answers;

    public void setAnswers(List<AnswerDto> answers) {
        this.answers = answers;
    }
}
