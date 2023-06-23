package com.codestates.back.domain.question.controller.dto;

import com.codestates.back.domain.answer.dto.AnswerDto;
import com.codestates.back.domain.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class QuestionAnswersDto {
    private Long id;
    private String title;
    private String body;
    private LocalDateTime createdAt;
    private UserDto.UserResponse user;
    List<AnswerDto> answers;

    public void setAnswers(List<AnswerDto> answers) {
        this.answers = answers;
    }

    public void setUser(UserDto.UserResponse user) {
        this.user = user;
    }
}
