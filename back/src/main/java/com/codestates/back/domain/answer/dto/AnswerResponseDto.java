package com.codestates.back.domain.answer.dto;

import com.codestates.back.domain.answer.entity.Answer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerResponseDto {
    private long answerId;
    private Answer.AnswerStatus answerStatus;
    private String body;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
