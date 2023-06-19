package com.codestates.back.domain.answer.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
public class AnswerPostDto {
    @Positive
    @NotNull
    private Long questionId;

    @NotBlank(message = "공백은 불가합니다")
    private String body;
}
