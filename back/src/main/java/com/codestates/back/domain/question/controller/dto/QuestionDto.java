package com.codestates.back.domain.question.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
public class QuestionDto {
    private Long id;
    private String title;
    private String body;
    private LocalDateTime createdAt;
    public void setQuestionId(Long id) {
        this.id = id;
    }
}
