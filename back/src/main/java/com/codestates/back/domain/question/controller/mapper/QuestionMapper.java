package com.codestates.back.domain.question.controller.mapper;

import com.codestates.back.domain.answer.dto.AnswerDto;
import com.codestates.back.domain.answer.entity.Answer;
import com.codestates.back.domain.question.controller.dto.QuestionAnswersDto;
import com.codestates.back.domain.question.controller.dto.QuestionDto;
import com.codestates.back.domain.question.domain.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    List<QuestionDto> questionToQuestionDtos(List<Question> questionV1s);

    Question postQuestionDtoToQuestion(QuestionDto postQuestionDto);

    @Mapping(source = "user", target = "user")
    QuestionDto questionToQuestionDto(Question question);

    @Mapping(source = "user", target = "user")
    QuestionAnswersDto questionToQuestionAnswersDto(Question question);

    List<AnswerDto> answersToAnswerDto(List<Answer> answers);
}
