package com.codestates.back.question;

import com.codestates.back.domain.answer.dto.AnswerDto;
import com.codestates.back.domain.answer.dto.EditDto;
import com.codestates.back.domain.question.application.QuestionService;
import com.codestates.back.domain.question.controller.QuestionController;
import com.codestates.back.domain.question.controller.dto.QuestionAnswersDto;
import com.codestates.back.domain.question.controller.dto.QuestionDto;
import com.codestates.back.domain.question.controller.mapper.QuestionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.codestates.back.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.codestates.back.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuestionController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class QuestionApiTest {
/*
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @MockBean
    private QuestionMapper questionMapper;

    @Autowired
    private Gson gson;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("특정 페이지 15개 질문 목록 조회")
    @Test
    public void getAllQuestionsTest() throws Exception {
        // given
        int pageNumber = 1;
        QuestionDto questionDto1 = new QuestionDto(
                1L,
                "질문제목1",
                "질문답변1",
                LocalDateTime.of(2023, 7, 7, 7, 7, 7)
        );
        QuestionDto questionDto2 = new QuestionDto(
                2L,
                "질문제목2",
                "질문답변2",
                LocalDateTime.of(2023, 7, 8, 7, 7, 7)
        );
        QuestionDto questionDto3 = new QuestionDto(
                3L,
                "질문제목3",
                "질문답변3",
                LocalDateTime.of(2023, 7, 9, 7, 7, 7)
        );
        List<QuestionDto> questionDtos = new ArrayList<>();
        questionDtos.add(questionDto3);
        questionDtos.add(questionDto2);
        questionDtos.add(questionDto1);
        given(questionService.countAllQuestions()).willReturn(3L);
        given(questionService.findQuestions(Mockito.anyInt())).willReturn(questionDtos);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get("/questions/page/{page-number}", pageNumber)
                );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allQuestionNumber").value(3))
                .andExpect(jsonPath("$.questionsList.[0].id").value(questionDtos.get(0).getId()))
                .andExpect(jsonPath("$.questionsList.[0].title").value(questionDtos.get(0).getTitle()))
                .andExpect(jsonPath("$.questionsList.[0].body").value(questionDtos.get(0).getBody()))
                .andExpect(jsonPath("$.questionsList.[1].id").value(questionDtos.get(1).getId()))
                .andExpect(jsonPath("$.questionsList.[1].title").value(questionDtos.get(1).getTitle()))
                .andExpect(jsonPath("$.questionsList.[1].body").value(questionDtos.get(1).getBody()))
                .andExpect(jsonPath("$.questionsList.[2].id").value(questionDtos.get(2).getId()))
                .andExpect(jsonPath("$.questionsList.[2].title").value(questionDtos.get(2).getTitle()))
                .andExpect(jsonPath("$.questionsList.[2].body").value(questionDtos.get(2).getBody()))
                .andDo(document("get-allQuestions",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("page-number").description("페이지 번호(최신순 정렬)")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("allQuestionNumber").type(JsonFieldType.NUMBER).description("DB에 저장된 전체질문 갯수"),
                                        fieldWithPath("questionsList").type(JsonFieldType.ARRAY).description("특정 페이지 최신순 15개 질문 목록"),
                                        fieldWithPath("questionsList[].id").type(JsonFieldType.NUMBER).description("질문 아이디"),
                                        fieldWithPath("questionsList[].title").type(JsonFieldType.STRING).description("질문 제목"),
                                        fieldWithPath("questionsList[].body").type(JsonFieldType.STRING).description("질문 내용"),
                                        fieldWithPath("questionsList[].createdAt").type(JsonFieldType.STRING).description("질문 최초 생성 시간")
                                )
                        )
                ));
    }

    @DisplayName("질문 등록 페이지 이동") // DisplayName은 필수는 아닙니다. 원하는대로 변경하시거나 지우셔도 됩니다.
    @Test
    public void directAskQuestionPageTest() throws Exception {
        // given
        // when
        ResultActions actions =
                mockMvc.perform(
                        get("/questions/ask")
                );

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("get-askQuestionPage",
                        getRequestPreProcessor(),
                        getResponsePreProcessor()
                ));
    }

    @DisplayName("질문 저장")
    @Test
    public void saveQuestionTest() throws Exception {
        // given
        QuestionDto questionDto = new QuestionDto(
                1L,
                "질문제목",
                "질문답변",
                LocalDateTime.of(2023, 7, 7, 7, 7, 7)
        );
        //String content = gson.toJson(questionDto);
        String content = objectMapper.writeValueAsString(questionDto);
        given(questionService.save(Mockito.any(QuestionDto.class), Mockito.anyLong())).willReturn(questionDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/questions/ask")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(questionDto.getId()))
                .andExpect(jsonPath("$.title").value(questionDto.getTitle()))
                .andExpect(jsonPath("$.body").value(questionDto.getBody()))
                .andDo(document("post-askQuestion",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("id").ignored(),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("질문 제목"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("질문 내용"),
                                        fieldWithPath("createdAt").ignored()
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("질문 아이디"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("질문 제목"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("질문 내용"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시간")
                                )
                        )
                ));
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
                        get("/questions/{question-id}", questionId)
                );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(questionAnswersDto.getId()))
                .andExpect(jsonPath("$.title").value(questionAnswersDto.getTitle()))
                .andExpect(jsonPath("$.answers[0].id").value(questionAnswersDto.getAnswers().get(0).getId()))
                .andExpect(jsonPath("$.answers[0].body").value(questionAnswersDto.getAnswers().get(0).getBody()))
                .andExpect(jsonPath("$.answers[1].id").value(questionAnswersDto.getAnswers().get(1).getId()))
                .andExpect(jsonPath("$.answers[1].body").value(questionAnswersDto.getAnswers().get(1).getBody()))
                .andDo(document("get-question-byQuestionId",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("question-id").description("질문 아이디")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("질문 아이디"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("질문 제목"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("질문 내용"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("질문 생성 시간"),
                                        fieldWithPath("answers").description("답변 리스트"),
                                        fieldWithPath("answers[].id").type(JsonFieldType.NUMBER).description("답변 아이디"),
                                        fieldWithPath("answers[].body").type(JsonFieldType.STRING).description("답변 내용"),
                                        fieldWithPath("answers[].createdAt").type(JsonFieldType.STRING).description("답변 생성 시간"),
                                        fieldWithPath("answers[].modifiedAt").type(JsonFieldType.STRING).description("답변 수정 시간")
                                )
                        )
                ));
    }

    @DisplayName("질문 수정 페이지 이동")
    @Test
    public void updateQuestionPageTest() throws Exception {
        // given
        long questionId = 1L;
        QuestionDto questionDto = new QuestionDto(
                1L,
                "질문 제목",
                "질문 했던 내용",
                LocalDateTime.of(2023, 7, 7, 7, 7, 7)
        );
        given(questionService.findQuestion(Mockito.anyLong())).willReturn(questionDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get("/questions/{question-id}/edit", questionId)
                );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value(questionDto.getBody()))
                .andDo(document("get-questionUpdatePage-byQuestionId",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("question-id").description("질문 아이디")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("질문 했던 내용")
                                )
                        )
                ));
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
                LocalDateTime.of(2023, 7, 7, 7, 7, 7)
        );
        questionDto.setQuestionId(questionId);
        //String content = gson.toJson(questionDto);
        String content = objectMapper.writeValueAsString(questionDto);
        given(questionService.updateQuestion(Mockito.any(QuestionDto.class))).willReturn(questionDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch("/questions/{question-id}/edit", questionId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(questionDto.getId()))
                .andExpect(jsonPath("$.title").value(questionDto.getTitle()))
                .andExpect(jsonPath("$.body").value(questionDto.getBody()))
                .andDo(document("patch-question-byQuestionId",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("question-id").description("질문 아이디")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("id").ignored(),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("질문 수정한 제목"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("질문 수정한 내용"),
                                        fieldWithPath("createdAt").ignored()
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("질문 아이디"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("질문 수정한 제목"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("질문 수정한 내용"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시간")
                                )
                        )
                ));
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
                        delete("/questions/{question-id}", questionId)
                );

        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-question-byQuestionId",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("question-id").description("질문 아이디")
                        )
                ));
    }

 */
}
