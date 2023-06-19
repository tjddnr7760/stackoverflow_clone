package com.codestates.back.domain.answer.dto;

import com.codestates.back.domain.answer.entity.Answer;
import lombok.Getter;
import lombok.Setter;

@Getter
public class AnswerPatchDto {
    @Setter
    private long answerId;
    private String body; //답 수정
    private Answer.AnswerStatus answerStatus; //답 삭제
}
