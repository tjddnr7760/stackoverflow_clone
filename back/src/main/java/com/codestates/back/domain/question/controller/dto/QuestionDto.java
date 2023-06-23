package com.codestates.back.domain.question.controller.dto;

import com.codestates.back.domain.user.dto.UserDto;
import com.codestates.back.domain.user.entity.User;
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
    private UserDto.UserResponse user;

    public void setQuestionId(Long id) {
        this.id = id;
    }

    public void setUser(UserDto.UserResponse user) {
        this.user = user;
    }
}
