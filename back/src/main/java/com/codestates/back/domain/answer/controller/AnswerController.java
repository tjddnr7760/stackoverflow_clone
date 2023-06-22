package com.codestates.back.domain.answer.controller;

import com.codestates.back.domain.answer.dto.AnswerDto;
import com.codestates.back.domain.answer.dto.EditDto;
import com.codestates.back.domain.answer.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

// 답변을 반납할때 질문정보와, 유저정보를 함께 반환한느게 맞지 않는가?

@RestController
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{answer-id}")
    public AnswerDto directAnswerPage(@PathVariable("answer-id") long answerId) {
        // 저장된 답변 페이지 아이디로 요청
        return answerService.findAnswer(answerId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{question-id}")
    public AnswerDto createAnswer(@RequestBody AnswerDto answerDto,
                                  @PathVariable("question-id") long questionId,
                                  Authentication authentication) {
        // 답변 저장
        return answerService.save(answerDto, questionId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{answer-id}/edit")
        public EditDto directAnswerEditPage(@PathVariable("answer-id") long answerId) {
        AnswerDto answerDto = answerService.findAnswer(answerId);
        EditDto editDto = new EditDto(answerDto.getBody());

        // 답변 수정 페이지 이동
        return editDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{answer-id}/edit")
    public AnswerDto editAnswer(@RequestBody AnswerDto answerDto,
                                @PathVariable("answer-id") long answerId,
                                Authentication authentication) {
        // 답변 수정
        return answerService.updateAnswer(answerId, answerDto);
    }

    @DeleteMapping("/{answer-id}")
    public ResponseEntity deleteAnswer(@PathVariable("answer-id") long answerId,
                                       Authentication authentication) {
        // 답변 삭제
        answerService.deleteAnswerById(answerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
