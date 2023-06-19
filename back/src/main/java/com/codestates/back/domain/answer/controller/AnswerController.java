package com.codestates.back.domain.answer.controller;

import com.codestates.back.domain.answer.dto.AnswerPatchDto;
import com.codestates.back.domain.answer.dto.AnswerPostDto;
import com.codestates.back.domain.answer.entity.Answer;
import com.codestates.back.domain.answer.mapper.AnswerMapper;
import com.codestates.back.domain.answer.service.AnswerService;
import com.codestates.back.domain.question.entity.Question;
import com.codestates.back.global.response.SingleResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/v1")
public class AnswerController {
    private AnswerService answerService;
    private AnswerMapper mapper;


// 답변작성 api
    @PostMapping("/user/answer/write")
    public ResponseEntity postAnswer(@Valid @RequestBody AnswerPostDto answerPostDto) {
        Answer question = answerService.createAnswer(
                mapper.answerPostDtoToAnswer(answerPostDto)
        );
        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.answerToAnswerResponseDto(question)), HttpStatus.CREATED
        );
    }

    //본인 답변수정, 본인 답만 수정 및 삭제 가능, 본인 답 아닌데 접근시 예외발생
    @PatchMapping("/user/answer/{answer-id}")
    public ResponseEntity patchAnswer(@PathVariable("answer-id") @Positive @NotNull long answerId,
                                      @Valid @RequestBody AnswerPatchDto answerPatchDto) {
        answerPatchDto.setAnswerId(answerId);
        Answer answer = mapper.answerPatchDtoToAnswer(answerService, answerPatchDto);
        Answer updatedAnswer = answerService.updateAnswer(answer);

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.answerToAnswerResponseDto(updatedAnswer)),
                HttpStatus.OK
        );
    }

}
