package com.codestates.back.domain.question.controller;

import com.codestates.back.domain.answer.dto.EditDto;
import com.codestates.back.domain.question.application.QuestionService;
import com.codestates.back.domain.question.controller.dto.QuestionAnswersDto;
import com.codestates.back.domain.question.controller.dto.QuestionDto;
import com.codestates.back.domain.question.controller.dto.QuestionsPageDto;
import com.codestates.back.domain.question.domain.Question;
import com.codestates.back.domain.user.entity.User;
import com.codestates.back.domain.user.service.UserService;
import com.codestates.back.global.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;

@Slf4j
//@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE})
@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    @Autowired
    public QuestionController(QuestionService questionService, UserService userService) {
        this.questionService = questionService;
        this.userService = userService;
    }

    @RequestMapping(value = "/questions/page/*", method = RequestMethod.OPTIONS)
    public void handleOptionsRequest(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    // 유저 정보 넘어와야함
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/page/{page-number}")
    public QuestionsPageDto directQuestionsPage(@PathVariable("page-number") int page) {
        // 질문 전체 목록 다 긁어와서 보내주기(페이징 기능)
        long size = questionService.countAllQuestions();
        List<QuestionDto> questionsDtoList = questionService.findQuestions(page);

        return new QuestionsPageDto(size, questionsDtoList);
    }

    @GetMapping("/ask")
    public ResponseEntity directAskQuestionPage(Authentication authentication) {
        // 질문 등록 페이지로 이동, 넘겨줄 데이터 없음
        // 토큰 있으면 바로 글쓰기 페이지로
        if (authentication.getName() != null) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/users/login")
                    .build()
                    .toUri();

            return ResponseEntity.created(location).body("로그인을 먼저 진행하세요");
        }
    }

    // 유저 정보 넘어와야함
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/ask")
    public QuestionDto askQuestion(@RequestBody QuestionDto postQuestionDto,
                                   Authentication authentication) {
        // 질문 제목, 내용 정보들 저장
        //임의로 유저 id 저장, 나중에 스프링 시큐리티로 유저아이디 가져와야함
        String email = authentication.getName();
        User userByEmail = userService.findUserByEmail(email);
        Long userId = userByEmail.getUserId();

        log.info("질문 내용 저장 = {}", postQuestionDto);
        QuestionDto resQuestionDto = questionService.save(postQuestionDto, userId);

        return resQuestionDto;
    }

    // 유저 정보 넘어와야함
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/specific/{questionId}")
    public QuestionAnswersDto directSpecificQuestionPage(@PathVariable("questionId") long questionId) {
        // 특정 질문 페이지 이동
        QuestionAnswersDto questionAnswersDto = questionService.findQuestionAnswers(questionId);

        return questionAnswersDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{questionId}/edit")
    public Object directQuestionEditPage(@PathVariable("questionId") long questionId,
                                         Authentication authentication) {
        if (authentication.getName() != null) {
            QuestionDto questionDto = questionService.findQuestion(questionId);
            EditDto editDto = new EditDto(questionDto.getBody());
            editDto.setTitle(questionDto.getTitle());

            // 질문 수정 페이지 이동
            return editDto;
        } else {
            return ErrorResponse.of(HttpStatus.NOT_ACCEPTABLE, "잘못된 요청입니다.");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{questionId}/edit")
    public Object editButtonQuestion(@PathVariable("questionId") long questionId,
                                     @RequestBody QuestionDto questionDto,
                                     Authentication authentication) {
        String email = authentication.getName();

        try {
            User userByEmail = userService.findUserByEmail(email);

            boolean containsAnswer = userByEmail.getQuestions()
                    .stream()
                    .map(Question::getId)
                    .anyMatch(id -> id.equals(questionId));

            if (containsAnswer) {
                questionDto.setQuestionId(questionId);
                QuestionDto resQuestionDto = questionService.updateQuestion(questionDto);

                return resQuestionDto;
            } else {
                return ErrorResponse.of(HttpStatus.NOT_ACCEPTABLE, "사용자가 등록한 질문이 아닙니다.");
            }
        } catch (Exception e) {
            return ErrorResponse.of(HttpStatus.NOT_ACCEPTABLE, "사용자 토큰이 DB에서 조회되지 않습니다.");
        }
    }

    @DeleteMapping("/{questionId}")
    public Object deleteButtonQuestion(@PathVariable("questionId") long questionId,
                                       Authentication authentication) {
        String email = authentication.getName();

        try {
            User userByEmail = userService.findUserByEmail(email);

            boolean containsAnswer = userByEmail.getQuestions()
                    .stream()
                    .map(Question::getId)
                    .anyMatch(id -> id.equals(questionId));

            if (containsAnswer) {
                questionService.deleteById(questionId);

                return new ResponseEntity(HttpStatus.NO_CONTENT);
            } else {
                return ErrorResponse.of(HttpStatus.NOT_ACCEPTABLE, "사용자가 등록한 질문이 아닙니다.");
            }
        } catch (Exception e) {
            return ErrorResponse.of(HttpStatus.NOT_ACCEPTABLE, "사용자 토큰이 DB에서 조회되지 않습니다.");
        }
    }
}
