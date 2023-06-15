package com.codestates.back.domain.question.controller;

import com.codestates.back.domain.question.application.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.RedisSessionProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/questions/ask")
    public ResponseEntity directAskQuestionPage() {

    }

    @PostMapping("/questions/ask")
    public ResponseEntity askQuestion() {

    }

    @GetMapping("/questions/{questions-id}")
    public ResponseEntity directSpecificQuestion() {

    }

    @GetMapping("/questions/{questions-id}/edit")
    public ResponseEntity directQuestionEditPage() {

    }

    @PatchMapping("/questions/{questions-id}/edit")
    public ResponseEntity editButtonQuestion() {

    }

    @DeleteMapping("/questions/{questions-id}")
    public ResponseEntity deleteButtonQuestion() {

    }
}
