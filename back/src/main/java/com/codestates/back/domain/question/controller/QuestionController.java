package com.codestates.back.domain.question.controller;

import com.codestates.back.domain.question.application.QuestionService;
import com.codestates.back.domain.question.controller.dto.QuestionAnswersDto;
import com.codestates.back.domain.question.controller.dto.QuestionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public ResponseEntity directQuestionsPage() {
        // 질문 전체 목록 다 긁어와서 보내주기(페이징 기능)
        List<QuestionDto> questionsDtoList = questionService.findAllQuestions();
        return new ResponseEntity(questionsDtoList, HttpStatus.OK);
    }

    @GetMapping("/ask")
    public ResponseEntity directAskQuestionPage() {
        // 질문 등록 페이지로 이동, 넘겨줄 데이터 없음
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/ask")
    public ResponseEntity askQuestion(@RequestBody QuestionDto postQuestionDto) {
        // 질문 제목, 내용 정보들 저장
        QuestionDto resQuestionDto = questionService.save(postQuestionDto);
        return new ResponseEntity(resQuestionDto, HttpStatus.OK);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity directSpecificQuestionPage(@PathVariable("questionId") long questionId) {
        // 특정 질문 페이지 이동
        QuestionAnswersDto questionAnswersDto = questionService.findQuestionAnswers(questionId);
        return new ResponseEntity(questionAnswersDto, HttpStatus.OK);
    }

    @GetMapping("/{questionId}/edit")
    public ResponseEntity directQuestionEditPage(@PathVariable("questionId") long questionsId) {
        // 질문 수정 페이지 이동
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/{questionId}/edit")
    public ResponseEntity editButtonQuestion(@PathVariable("questionId") long questionId,
                                             @RequestBody QuestionDto questionDto) {
        // 수정 버튼 누를시
        questionDto.setQuestionId(questionId);
        QuestionDto resQuestionDto = questionService.updateQuestion(questionDto);

        return new ResponseEntity(resQuestionDto, HttpStatus.OK);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity deleteButtonQuestion(@PathVariable("questionsId") long questionId) {
        // 삭제 버튼 누를시
        questionService.deleteById(questionId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
