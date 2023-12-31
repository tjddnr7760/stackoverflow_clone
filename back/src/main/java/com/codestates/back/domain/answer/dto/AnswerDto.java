package com.codestates.back.domain.answer.dto;

import com.codestates.back.domain.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AnswerDto {
    private Long id;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private UserDto.UserResponse user;

    public void setUser(UserDto.UserResponse user) {
        this.user = user;
    }
}