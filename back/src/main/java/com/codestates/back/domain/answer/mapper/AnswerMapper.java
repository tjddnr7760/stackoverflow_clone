package com.codestates.back.domain.answer.mapper;

import com.codestates.back.domain.answer.dto.AnswerPatchDto;
import com.codestates.back.domain.answer.dto.AnswerPostDto;
import com.codestates.back.domain.answer.dto.AnswerResponseDto;
import com.codestates.back.domain.answer.entity.Answer;
import com.codestates.back.domain.answer.service.AnswerService;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    default Answer answerPostDtoToAnswer(AnswerPostDto answerPostDto){
        Answer answer = new Answer();
        answer.setBody(answerPostDto.getBody());

        return answer;
    }
    default Answer answerPatchDtoToAnswer(AnswerService answerService, AnswerPatchDto answerPatchDto) {
    Answer answer = new Answer();
    answer.setAnswerId(answerPatchDto.getAnswerId());
    answer.setBody(answerPatchDto.getBody());
    answer.setAnswerStatus(answerPatchDto.getAnswerStatus());

    return answer;
    }

    default AnswerResponseDto answerToAnswerResponseDto(Answer answer) {
        AnswerResponseDto answerResponseDto = new AnswerResponseDto();
        answerResponseDto.setAnswerId(answer.getAnswerId());
        answerResponseDto.setAnswerStatus(answer.getAnswerStatus());
        answerResponseDto.setBody(answer.getBody());
        answerResponseDto.setCreatedAt(answer.getCreatedAt());

        return answerResponseDto;
    }

    List<AnswerResponseDto> answersToAnswerResponseDtos(List<Answer> answers);

}
