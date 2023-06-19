package com.codestates.back.domain.answer.controller;

import com.codestates.back.domain.answer.dto.AnswerDto;
import com.codestates.back.domain.answer.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("{question-id}")
    public AnswerDto createAnswer(@RequestBody AnswerDto answerDto,
                                  @PathVariable("question-id") long questionId) {
        // 답변 저장
        return answerService.save(answerDto, questionId);
    }

    @GetMapping("/{answer-id}/edit")
        public ResponseEntity directAnswerEditPage(@PathVariable("answer-id") long answerId) {
        // 답변 페이지 이동
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{answer-id}/edit")
    public AnswerDto editAnswer(@RequestBody AnswerDto answerDto,
                                @PathVariable("answer-id") long answerId) {
        // 답변 수정
        return answerService.updateAnswer(answerId, answerDto);
    }

    @DeleteMapping("/{answer-id}")
    public ResponseEntity deleteAnswer(@PathVariable("answer-id") long answerId) {
        // 답변 삭제
        answerService.deleteAnswerById(answerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
