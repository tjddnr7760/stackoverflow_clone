package com.codestates.back.domain.question.application;

import com.codestates.back.domain.answer.dto.AnswerDto;
import com.codestates.back.domain.answer.entity.Answer;
import com.codestates.back.domain.question.controller.dto.QuestionAnswersDto;
import com.codestates.back.domain.question.controller.dto.QuestionDto;
import com.codestates.back.domain.question.controller.mapper.QuestionMapper;
import com.codestates.back.domain.question.domain.Question;
import com.codestates.back.domain.question.infrastructure.QuestionRepository;
import com.codestates.back.domain.user.dto.UserDto;
import com.codestates.back.domain.user.entity.User;
import com.codestates.back.domain.user.mapper.UserMapper;
import com.codestates.back.domain.user.repository.UserRepository;
import com.codestates.back.global.exception.BusinessLogicException;
import com.codestates.back.global.exception.exceptioncode.ExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class QuestionServiceV1 implements QuestionService{

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuestionMapper questionMapper;
    private final UserMapper userMapper;

    @Autowired
    public QuestionServiceV1(QuestionRepository questionRepository, UserRepository userRepository,
                             QuestionMapper questionMapper, UserMapper userMapper) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.questionMapper = questionMapper;
        this.userMapper = userMapper;
    }

    // 유저정보 넘어와야함
    @Override
    public List<QuestionDto> findQuestions(int page) {
        int pageSize = 15;
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by("createdAt").descending());
        Page<Question> result = questionRepository.findAll(pageRequest);

        List<QuestionDto> questionDtos = new ArrayList<>();
        for (Question question : result.getContent()) {
            QuestionDto questionDto = questionMapper.questionToQuestionDto(question);
            questionDtos.add(questionDto);
        }

        return questionDtos;
    }

    @Override
    public long countAllQuestions() {
        return questionRepository.count();
    }

    @Override
    public QuestionDto findQuestion(long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Question question = optionalQuestion.orElseThrow(() ->
                // 질문 아이디로 질문 못찾을시
                new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));

        return questionMapper.questionToQuestionDto(question);
    }

    @Override
    public QuestionDto save(QuestionDto postQuestionDto, long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() ->
                // 유저 아이디로 유저 못찾을시
                new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        Question question = questionMapper.postQuestionDtoToQuestion(postQuestionDto);
        question.setUser(user);
        user.getQuestions().add(question);
        question = questionRepository.save(question);
        QuestionDto questionDto = questionMapper.questionToQuestionDto(question);

        return questionDto;
    }

    @Override
    public QuestionAnswersDto findQuestionAnswers(long questionsId) {
        // 여기서 질문 아이디로 조회하면 답변들도 다 가져와야함
        Optional<Question> optionalQuestion = questionRepository.findById(questionsId);
        Question question = optionalQuestion.orElseThrow(() ->
                // 질문 아이디로 질문 못찾을시
                new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));

        List<Answer> answers = question.getAnswers();
        QuestionAnswersDto questionAnswersDto = questionMapper.questionToQuestionAnswersDto(question);
        List<AnswerDto> answerDto = questionMapper.answersToAnswerDto(answers);
        questionAnswersDto.setAnswers(answerDto);

        return questionAnswersDto;
    }

    @Override
    public QuestionDto updateQuestion(QuestionDto questionDto) {
        Optional<Question> optionalQuestionV1 = questionRepository.findById(questionDto.getId());
        Question question = optionalQuestionV1.orElseThrow(() ->
                // 질문 아이디로 질문 못찾을시
                new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));

        question.update(questionDto);
        return questionMapper.questionToQuestionDto(questionRepository.save(question));
    }

    @Override
    public void deleteById(long questionId) {
        questionRepository.deleteById(questionId);
    }
}
