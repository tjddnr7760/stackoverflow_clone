package com.codestates.back.question;

import com.codestates.back.domain.answer.dto.AnswerDto;
import com.codestates.back.domain.question.application.QuestionService;
import com.codestates.back.domain.question.controller.QuestionController;
import com.codestates.back.domain.question.controller.dto.QuestionAnswersDto;
import com.codestates.back.domain.question.controller.dto.QuestionDto;
import com.codestates.back.domain.question.controller.mapper.QuestionMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuestionController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class QuestionApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @MockBean
    private QuestionMapper questionMapper;

    @Autowired
    private Gson gson;

    @DisplayName("질문 전체 목록") // DisplayName은 필수는 아닙니다. 원하는대로 변경하시거나 지우셔도 됩니다.
    @Test
    public void getAllQuestionsTest() throws Exception {
        // given
        QuestionDto questionDto1 = new QuestionDto(
                1L,
                "질문제목1",
                "질문답변1",
                null
        );
        QuestionDto questionDto2 = new QuestionDto(
                2L,
                "질문제목2",
                "질문답변2",
                null
        );
        List<QuestionDto> questionDtos = new ArrayList<>();
        questionDtos.add(questionDto1);
        questionDtos.add(questionDto2);
        given(questionService.findAllQuestions()).willReturn(questionDtos);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get("/question")
                );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(questionDtos.get(0).getId()))
                .andExpect(jsonPath("$[0].title").value(questionDtos.get(0).getTitle()))
                .andExpect(jsonPath("$[0].body").value(questionDtos.get(0).getBody()))
                .andExpect(jsonPath("$[1].id").value(questionDtos.get(1).getId()))
                .andExpect(jsonPath("$[1].title").value(questionDtos.get(1).getTitle()))
                .andExpect(jsonPath("$[1].body").value(questionDtos.get(1).getBody()));
    }

    @DisplayName("질문 등록 페이지 이동") // DisplayName은 필수는 아닙니다. 원하는대로 변경하시거나 지우셔도 됩니다.
    @Test
    public void directAskQuestionPageTest() throws Exception {
        // given
        // when
        ResultActions actions =
                mockMvc.perform(
                        get("/question/ask")
                );

        // then
        actions
                .andExpect(status().isOk());
    }

    @DisplayName("질문 저장")
    @Test
    public void saveQuestionTest() throws Exception {
        // given
        QuestionDto questionDto = new QuestionDto(
                1L,
                "질문제목",
                "질문답변",
                null
        );
        String content = gson.toJson(questionDto);
        given(questionService.save(Mockito.any(QuestionDto.class), Mockito.anyLong())).willReturn(questionDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/question/ask")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(questionDto.getId()))
                .andExpect(jsonPath("$.title").value(questionDto.getTitle()))
                .andExpect(jsonPath("$.body").value(questionDto.getBody()));
    }

    @DisplayName("특정 질문페이지 이동")
    @Test
    public void directSpecificQuestionPageTest() throws Exception {
        // given
        long questionId = 1L;
        AnswerDto answerDto1 = new AnswerDto(
                1L, "답변1",
                LocalDateTime.of(2023, 7, 7, 7, 7, 7),
                LocalDateTime.of(2023, 7, 7, 7, 7, 7)
        );
        AnswerDto answerDto2 = new AnswerDto(
                2L, "답변2",
                LocalDateTime.of(2023, 7, 7, 7, 7, 7),
                LocalDateTime.of(2023, 7, 7, 7, 7, 7)
        );
        List<AnswerDto> answers = new ArrayList<>();
        answers.add(answerDto1);
        answers.add(answerDto2);
        QuestionAnswersDto questionAnswersDto = new QuestionAnswersDto(
                1L,
                "질문제목", "질문내용",
                LocalDateTime.of(2023, 7, 7, 7, 7, 7),
                answers
        );
        given(questionService.findQuestionAnswers(Mockito.anyLong())).willReturn(questionAnswersDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get("/question/" + questionId)
                );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(questionAnswersDto.getId()))
                .andExpect(jsonPath("$.title").value(questionAnswersDto.getTitle()))
                .andExpect(jsonPath("$.answers[0].id").value(questionAnswersDto.getAnswers().get(0).getId()))
                .andExpect(jsonPath("$.answers[0].body").value(questionAnswersDto.getAnswers().get(0).getBody()))
                .andExpect(jsonPath("$.answers[1].id").value(questionAnswersDto.getAnswers().get(1).getId()))
                .andExpect(jsonPath("$.answers[1].body").value(questionAnswersDto.getAnswers().get(1).getBody()));
    }

    @DisplayName("질문 수정 페이지 이동")
    @Test
    public void updateQuestionPageTest() throws Exception {
        // given
        // when
        ResultActions actions =
                mockMvc.perform(
                        get("/question/1/edit")
                );

        // then
        actions
                .andExpect(status().isOk());
    }

    @DisplayName("질문 수정")
    @Test
    public void updateQuestionTest() throws Exception {
        // given
        long questionId = 2L;
        QuestionDto questionDto = new QuestionDto(
                1L,
                "질문제목",
                "질문답변",
                null
        );
        questionDto.setQuestionId(questionId);
        String content = gson.toJson(questionDto);
        given(questionService.updateQuestion(Mockito.any(QuestionDto.class))).willReturn(questionDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch("/question/" + questionId + "/edit")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(questionDto.getId()))
                .andExpect(jsonPath("$.title").value(questionDto.getTitle()))
                .andExpect(jsonPath("$.body").value(questionDto.getBody()));
    }

    @DisplayName("질문 삭제")
    @Test
    public void deleteQuestionTest() throws Exception {
        // given
        long questionId = 1L;
        ResponseEntity response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        doNothing().when(questionService).deleteById(Mockito.anyLong());

        // when
        ResultActions actions =
                mockMvc.perform(
                        delete("/question/" + questionId)
                );

        // then
        actions
                .andExpect(status().isNoContent());
    }
}
