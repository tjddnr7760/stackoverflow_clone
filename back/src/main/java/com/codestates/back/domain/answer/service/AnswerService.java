package com.codestates.back.domain.answer.service;

import com.codestates.back.domain.answer.AnswerRepository.AnswerRepository;
import com.codestates.back.domain.answer.dto.AnswerDto;
import com.codestates.back.domain.answer.dto.EditAnswerDto;
import com.codestates.back.domain.answer.entity.Answer;
import com.codestates.back.domain.answer.mapper.AnswerMapper;
import com.codestates.back.domain.question.domain.Question;
import com.codestates.back.domain.question.infrastructure.QuestionRepository;
import com.codestates.back.domain.user.entity.User;
import com.codestates.back.global.exception.BusinessLogicException;
import com.codestates.back.global.exception.exceptioncode.ExceptionCode;
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

    public AnswerDto save(User user, AnswerDto answerDto, long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Question question = optionalQuestion.orElseThrow(() ->
                // 질문아이디로 질문 db에서 못찾을시
                new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        Answer answer = answerMapper.answerDtoToAnswer(answerDto);
        answer.setQuestion(question);
        question.getAnswers().add(answer);
        answer.setUser(user);
        user.getAnswers().add(answer);
        Answer save = answerRepository.save(answer);

        return answerMapper.answerToAnswerDto(save);
    };

    public AnswerDto findAnswer(long answerId) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        Answer answer = optionalAnswer.orElseThrow(() ->
                // 답변 db에서 조회 못찾을시
                new BusinessLogicException(ExceptionCode.ANSWER_NOT_FOUND));

        return answerMapper.answerToAnswerDto(answer);
    };

    public EditAnswerDto updateAnswer(long answerId, AnswerDto answerDto) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        Answer answer = optionalAnswer.orElseThrow(() ->
                // 답변 db에서 조회 못찾을시
                new BusinessLogicException(ExceptionCode.ANSWER_NOT_FOUND));
        Answer update = answer.update(answerDto);
        Answer save = answerRepository.save(update);

        return answerMapper.answerToEditAnswerDto(save);
    };

    public void deleteAnswerById(long answerId) {
        answerRepository.deleteById(answerId);
    };
}
