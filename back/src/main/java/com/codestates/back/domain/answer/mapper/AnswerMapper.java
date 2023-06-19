package com.codestates.back.domain.answer.mapper;

import com.codestates.back.domain.answer.dto.AnswerDto;
import com.codestates.back.domain.answer.entity.Answer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    Answer answerDtoToAnswer(AnswerDto answerDto);
    AnswerDto answerToAnswerDto(Answer save);

}
