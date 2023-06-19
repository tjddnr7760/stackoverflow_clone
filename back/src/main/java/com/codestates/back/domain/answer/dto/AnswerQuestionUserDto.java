package com.codestates.back.domain.answer.dto;

import com.codestates.back.domain.answer.entity.Answer;
import com.codestates.back.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AnswerQuestionUserDto {
    private Long id;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private User user;
    private Answer answer;

    public AnswerQuestionUserDto(Long id, String body) {
        this.id = id;
        this.body = body;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
