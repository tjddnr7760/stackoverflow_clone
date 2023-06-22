package com.codestates.back.domain.question.controller;

import com.codestates.back.domain.question.application.QuestionService;
import com.codestates.back.domain.question.controller.dto.QuestionAnswersDto;
import com.codestates.back.domain.question.controller.dto.QuestionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<QuestionDto> directQuestionsPage() {
        // 질문 전체 목록 다 긁어와서 보내주기(페이징 기능)
        List<QuestionDto> questionsDtoList = questionService.findAllQuestions();

        return questionsDtoList;
    }

    @GetMapping("/ask")
    public ResponseEntity directAskQuestionPage() {
        // 질문 등록 페이지로 이동, 넘겨줄 데이터 없음
        // 토큰 있으면 바로 글쓰기 페이지로

        // 토큰 없으면 로그인 페이지로 리다이렉트

        return new ResponseEntity(HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/ask")
    public QuestionDto askQuestion(@RequestBody QuestionDto postQuestionDto,
                                   Authentication authentication) {
        // 질문 제목, 내용 정보들 저장
        //임의로 유저 id 저장, 나중에 스프링 시큐리티로 유저아이디 가져와야함
        Long userId = 1L;

        log.info("질문 내용 저장 = {}", postQuestionDto);
        QuestionDto resQuestionDto = questionService.save(postQuestionDto, userId);

        return resQuestionDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{questionId}")
    public QuestionAnswersDto directSpecificQuestionPage(@PathVariable("questionId") long questionId) {
        // 특정 질문 페이지 이동
        QuestionAnswersDto questionAnswersDto = questionService.findQuestionAnswers(questionId);

        return questionAnswersDto;
    }

    @GetMapping("/{questionId}/edit")
    public ResponseEntity directQuestionEditPage(@PathVariable("questionId") long questionId) {
        // 질문 수정 페이지 이동
        return new ResponseEntity(HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{questionId}/edit")
    public QuestionDto editButtonQuestion(@PathVariable("questionId") long questionId,
                                          @RequestBody QuestionDto questionDto,
                                          Authentication authentication) {
        // 수정 버튼 누를시
        questionDto.setQuestionId(questionId);
        QuestionDto resQuestionDto = questionService.updateQuestion(questionDto);

        return resQuestionDto;
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity deleteButtonQuestion(@PathVariable("questionId") long questionId,
                                               Authentication authentication) {
        // 삭제 버튼 누를시
        questionService.deleteById(questionId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
