package com.codestates.back.domain.answer.service;

import com.codestates.back.domain.answer.AnswerRepository.AnswerRepository;
import com.codestates.back.domain.answer.entity.Answer;
import com.codestates.back.global.exception.BusinessLogicException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;

    }

    public Answer createAnswer(Answer answer){
        return answerRepository.save(answer);
    }

    public Answer findVerifiedAnswer (long answerId) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        Answer findAnswer = optionalAnswer.orElseThrow(() ->
                new BusinessLogicException());
        return findAnswer;
    }

    public Answer updateAnswer(Answer answer){
        Answer findAnswer = findVerifiedAnswer(answer.getAnswerId());

        Optional.ofNullable(answer.getBody())
                .ifPresent(answerBody -> findAnswer.setBody(answerBody)); //내용수정

        Optional.ofNullable(answer.getAnswerStatus())
                .ifPresent(answerStatus -> findAnswer.setAnswerStatus(answerStatus)); //글삭제

        Answer updatedQuestion = answerRepository.save(findAnswer);

        return updatedQuestion;
    }



}
