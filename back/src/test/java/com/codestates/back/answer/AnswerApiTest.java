package com.codestates.back.answer;

import com.codestates.back.domain.answer.controller.AnswerController;
import com.codestates.back.domain.answer.dto.AnswerDto;
import com.codestates.back.domain.answer.mapper.AnswerMapper;
import com.codestates.back.domain.answer.service.AnswerService;
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
import java.util.List;

import static com.codestates.back.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.codestates.back.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnswerController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class AnswerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnswerService answerService;

    @MockBean
    private AnswerMapper answerMapper;

    @Autowired
    private Gson gson;

    @DisplayName("답변 아이디 조회")
    @Test
    public void searchAnswerTest() throws Exception {
        // given
        long answerId = 1L;
        AnswerDto answerDto = new AnswerDto(
                1L,
                "답변내용",
                LocalDateTime.of(2023, 7, 7, 7, 7, 7),
                LocalDateTime.of(2023, 7, 7, 7, 7, 7)
        );
        given(answerService.findAnswer(Mockito.anyLong())).willReturn(answerDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get("/answer/" + answerId)
                );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(answerDto.getId()))
                .andExpect(jsonPath("$.body").value(answerDto.getBody()))
                .andDo(document("get-answer-byAnswerId",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                               parameterWithName("answer-id").description("답변 아이디")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath().type().description(),
                                )
                        )
                ));
    }

    @DisplayName("답변 저장")
    @Test
    public void saveAnswerTest() throws Exception {
        // given
        long questionId = 1L;
        AnswerDto answerDto = new AnswerDto(
                1L,
                "답변내용",
                null, null
        );
        String content = gson.toJson(answerDto);
        given(answerService.save(Mockito.any(AnswerDto.class), Mockito.anyLong())).willReturn(answerDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/answer/" + questionId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(answerDto.getId()))
                .andExpect(jsonPath("$.body").value(answerDto.getBody()))
                .andDo(document("post-saveAnswer-byQuestionId",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("question-id").description("질문 아이디")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("답변 내용")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath().type().description(),
                                )
                        )
                ));
    }

    @DisplayName("답변 페이지 이동")
    @Test
    public void directAnswerPageTest() throws Exception {
        // given
        long answerId = 1L;
        ResponseEntity response = new ResponseEntity<>(HttpStatus.OK);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get("/answer/" + answerId + "/edit")
                );

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("get-answerPage-byAnswerId",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("answer-id").description("답변 아이디")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath().type().description(),
                                )
                        )
                ));
    }

    @DisplayName("답변 수정")
    @Test
    public void updateAnswerTest() throws Exception {
        // given
        long answerId = 1L;
        AnswerDto answerDto = new AnswerDto(
                1L,
                "답변내용",
                null, null
        );
        String content = gson.toJson(answerDto);
        given(answerService.updateAnswer(Mockito.anyLong(), Mockito.any(AnswerDto.class))).willReturn(answerDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch("/answer/" + answerId + "/edit")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(answerDto.getId()))
                .andExpect(jsonPath("$.body").value(answerDto.getBody()))
                .andDo(document("patch-answer-byAnswerId",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("answer-id").description("답변 아이디")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("답변 수정 내용")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath().type().description(),
                                )
                        )
                ));
    }

    @DisplayName("답변 삭제")
    @Test
    public void deleteAnswerTest() throws Exception {
        // given
        long answerId = 1L;
        ResponseEntity response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        doNothing().when(answerService).deleteAnswerById(Mockito.anyLong());

        // when
        ResultActions actions =
                mockMvc.perform(
                        delete("/answer/" + answerId)
                );

        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-answer-byAnswerId",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("answer-id").description("답변 아이디")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath().type().description(),
                                )
                        )
                ));
    }
}
