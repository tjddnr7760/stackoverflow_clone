package com.codestates.back.domain.question.application;

import com.codestates.back.domain.answer.dto.AnswerDto;
import com.codestates.back.domain.answer.entity.Answer;
import com.codestates.back.domain.question.controller.dto.QuestionAnswersDto;
import com.codestates.back.domain.question.controller.dto.QuestionDto;
import com.codestates.back.domain.question.controller.mapper.QuestionMapper;
import com.codestates.back.domain.question.domain.Question;
import com.codestates.back.domain.question.infrastructure.QuestionRepository;
import com.codestates.back.domain.user.entity.User;
import com.codestates.back.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class QuestionServiceV1 implements QuestionService{

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuestionMapper questionMapper;

    @Autowired
    public QuestionServiceV1(QuestionRepository questionRepository, UserRepository userRepository, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.questionMapper = questionMapper;
    }

    @Override
    public List<QuestionDto> findAllQuestions() {
        List<Question> questionV1s = questionRepository.findAll();
        List<QuestionDto> questionDtos = questionMapper.questionToQuestionDtos(questionV1s);
        return questionDtos;
    }

    @Override
    public QuestionDto save(QuestionDto postQuestionDto, long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() ->
                new RuntimeException());

        Question question = questionMapper.postQuestionDtoToQuestion(postQuestionDto);
        question.setUser(user);
        user.getQuestions().add(question);
        question = questionRepository.save(question);
        return questionMapper.questionToQuestionDto(question);
    }

    @Override
    public QuestionAnswersDto findQuestionAnswers(long questionsId) {
        // 여기서 질문 아이디로 조회하면 답변들도 다 가져와야함
        Optional<Question> optionalQuestion = questionRepository.findById(questionsId);
        Question question = optionalQuestion.orElseThrow(() ->
                new RuntimeException());
        List<Answer> answers = question.getAnswers();
        QuestionAnswersDto questionAnswersDto = questionMapper.questionToQuestionAnswersDto(question);
        List<AnswerDto> answerDto = questionMapper.answersToAnswerDto(answers);
        questionAnswersDto.setAnswers(answerDto);

        return questionAnswersDto;
    }

    @Override
    public QuestionDto updateQuestion(QuestionDto questionDto) {
        Optional<Question> optionalQuestionV1 = questionRepository.findById(questionDto.getQuestionId());
        Question questionV1 = optionalQuestionV1.orElseThrow(() ->
                new RuntimeException());

        questionV1.update(questionDto);
        return questionMapper.questionToQuestionDto(questionRepository.save(questionV1));
    }

    @Override
    public void deleteById(long questionId) {
        questionRepository.deleteById(questionId);
    }
}
