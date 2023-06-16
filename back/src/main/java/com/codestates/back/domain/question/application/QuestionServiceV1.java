package com.codestates.back.domain.question.application;

import com.codestates.back.domain.question.controller.dto.QuestionAnswersDto;
import com.codestates.back.domain.question.controller.dto.QuestionDto;
import com.codestates.back.domain.question.controller.mapper.QuestionMapper;
import com.codestates.back.domain.question.domain.QuestionV1;
import com.codestates.back.domain.question.infrastructure.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceV1 implements QuestionService{
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    @Autowired
    public QuestionServiceV1(QuestionRepository questionRepository, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    @Override
    public List<QuestionDto> findAllQuestions() {
        List<QuestionV1> questionV1s = questionRepository.findAll();
        List<QuestionDto> questionDtos = questionMapper.questionV1ToQuestionDtos(questionV1s);
        return questionDtos;
    }

    @Override
    public QuestionDto save(QuestionDto postQuestionDto) {
        QuestionV1 question = questionMapper.postQuestionDtoToQuestionV1(postQuestionDto);

        System.out.println(question.getTitle());
        System.out.println(question.getBody());

        question = questionRepository.save(question);
        return questionMapper.questionv1ToQuestionDto(question);
    }

    @Override
    public QuestionAnswersDto findQuestionAnswers(long questionsId) {
        // 여기서 질문 아이디로 조회하면 답변들도 다 가져와야함
        return null;
    }

    @Override
    public QuestionDto updateQuestion(QuestionDto questionDto) {
        Optional<QuestionV1> optionalQuestionV1 = questionRepository.findById(questionDto.getQuestionId());
        QuestionV1 questionV1 = optionalQuestionV1.orElseThrow(() ->
                new RuntimeException());

        questionV1.update(questionDto);
        return questionMapper.questionv1ToQuestionDto(questionRepository.save(questionV1));
    }

    @Override
    public void deleteById(long questionId) {
        questionRepository.deleteById(questionId);
    }
}
