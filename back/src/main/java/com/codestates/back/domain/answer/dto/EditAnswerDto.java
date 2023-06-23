package com.codestates.back.domain.answer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EditAnswerDto {
    private Long id;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
