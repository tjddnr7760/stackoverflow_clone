package com.codestates.back.domain.question.controller.mapper;

import com.codestates.back.domain.question.controller.dto.QuestionDto;
import com.codestates.back.domain.question.domain.QuestionV1;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    List<QuestionDto> questionV1ToQuestionDtos(List<QuestionV1> questionV1s);
    QuestionV1 postQuestionDtoToQuestionV1(QuestionDto postQuestionDto);
    QuestionDto questionv1ToQuestionDto(QuestionV1 question);
}
