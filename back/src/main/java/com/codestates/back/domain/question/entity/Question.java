package com.codestates.back.domain.question.domain;

import com.codestates.back.domain.question.controller.dto.QuestionDto;

public interface Question {
    public abstract void update(QuestionDto questionDto);
}