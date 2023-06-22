package com.codestates.back.domain.answer.controller;

import com.codestates.back.domain.answer.dto.AnswerDto;
import com.codestates.back.domain.answer.dto.EditAnswerDto;
import com.codestates.back.domain.answer.dto.EditDto;
import com.codestates.back.domain.answer.service.AnswerService;
import com.codestates.back.domain.user.entity.User;
import com.codestates.back.domain.user.service.UserService;
import com.codestates.back.global.exception.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;
    private final UserService userService;

    @Autowired
    public AnswerController(AnswerService answerService, UserService userService) {
        this.answerService = answerService;
        this.userService = userService;
    }

    // 유저 정보 넘어와야함
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/specific/{answer-id}")
    public AnswerDto directAnswerPage(@PathVariable("answer-id") long answerId) {
        // 저장된 답변 페이지 아이디로 요청
        return answerService.findAnswer(answerId);
    }

    // 유저 정보 넘어와야함
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{question-id}")
    public AnswerDto createAnswer(@RequestBody AnswerDto answerDto,
                                  @PathVariable("question-id") long questionId,
                                  Authentication authentication) {
        // 답변 저장
        String email = authentication.getName();
        User userByEmail = userService.findUserByEmail(email);

        return answerService.save(userByEmail, answerDto, questionId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{answer-id}/edit")
        public Object directAnswerEditPage(@PathVariable("answer-id") long answerId,
                                            Authentication authentication) {
        if (authentication.getName() != null) {
            AnswerDto answerDto = answerService.findAnswer(answerId);
            EditDto editDto = new EditDto(answerDto.getBody());

            // 답변 수정 페이지 이동
            return editDto;
        } else {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/users/login")
                    .build()
                    .toUri();

            return ResponseEntity.created(location).body("로그인을 먼저 진행하세요");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{answer-id}/edit")
    public Object editAnswer(@RequestBody AnswerDto answerDto,
                                @PathVariable("answer-id") long answerId,
                                Authentication authentication) {
        // 답변 수정
        String email = authentication.getName();
        User userByEmail = userService.findUserByEmail(email);
        if (userByEmail.getAnswers().contains(answerService.findAnswer(answerId))) {
            EditAnswerDto editAnswerDto = answerService.updateAnswer(answerId, answerDto);

            return editAnswerDto;
        } else {
            return ErrorResponse.of(HttpStatus.NOT_ACCEPTABLE, "사용자가 등록한 답변이 아닙니다.");
        }
    }

    @DeleteMapping("/{answer-id}")
    public Object deleteAnswer(@PathVariable("answer-id") long answerId,
                                       Authentication authentication) {
        // 답변 삭제
        String email = authentication.getName();
        User userByEmail = userService.findUserByEmail(email);
        if (userByEmail.getAnswers().contains(answerService.findAnswer(answerId))) {
            answerService.deleteAnswerById(answerId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ErrorResponse.of(HttpStatus.NOT_ACCEPTABLE, "사용자가 등록한 답변이 아닙니다.");
        }
    }
}
