package com.codestates.back.domain.question.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class QuestionDto {
    private Long questionId;
    private String title;
    private String body;
    private Date createdAt;

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
}
