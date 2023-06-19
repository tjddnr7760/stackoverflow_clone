package com.codestates.back.domain.answer.service;

import com.codestates.back.domain.answer.AnswerRepository.AnswerRepository;
import com.codestates.back.domain.answer.dto.AnswerDto;
import com.codestates.back.domain.answer.entity.Answer;
import com.codestates.back.domain.answer.mapper.AnswerMapper;
import com.codestates.back.domain.question.domain.Question;
import com.codestates.back.domain.question.infrastructure.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final AnswerMapper answerMapper;

    @Autowired
    public AnswerService(AnswerRepository answerRepository, QuestionRepository questionRepository, AnswerMapper answerMapper) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.answerMapper = answerMapper;
    }

    public AnswerDto save(AnswerDto answerDto, long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Question question = optionalQuestion.orElseThrow(() ->
                new RuntimeException());

        Answer answer = answerMapper.answerDtoToAnswer(answerDto);
        answer.setQuestion(question);
        question.getAnswers().add(answer);
        Answer save = answerRepository.save(answer);

        return answerMapper.answerToAnswerDto(save);
    };

    public AnswerDto findAnswer(long answerId) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        Answer answer = optionalAnswer.orElseThrow(() ->
                new RuntimeException());

        return answerMapper.answerToAnswerDto(answer);
    };

    public AnswerDto updateAnswer(long answerId, AnswerDto answerDto) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        Answer answer = optionalAnswer.orElseThrow(() ->
                new RuntimeException());
        Answer update = answer.update(answerDto);
        Answer save = answerRepository.save(update);

        return answerMapper.answerToAnswerDto(save);
    };

    public void deleteAnswerById(long answerId) {
        answerRepository.deleteById(answerId);
    };
}
