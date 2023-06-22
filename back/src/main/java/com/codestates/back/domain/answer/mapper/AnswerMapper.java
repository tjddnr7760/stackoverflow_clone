package com.codestates.back.domain.answer.mapper;

import com.codestates.back.domain.answer.dto.AnswerDto;
import com.codestates.back.domain.answer.dto.EditAnswerDto;
import com.codestates.back.domain.answer.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    Answer answerDtoToAnswer(AnswerDto answerDto);

    @Mapping(source = "user", target = "user")
    AnswerDto answerToAnswerDto(Answer save);

    EditAnswerDto answerToEditAnswerDto(Answer save);
}
