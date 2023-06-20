package com.codestates.back.user;

import com.codestates.back.domain.user.controller.UserController;
import com.codestates.back.domain.user.dto.UserDto;
import com.codestates.back.domain.user.entity.User;
import com.codestates.back.domain.user.mapper.UserMapper;
import com.codestates.back.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import com.google.gson.Gson;
import org.springframework.test.web.servlet.ResultActions;


import org.springframework.transaction.annotation.Transactional;

import static com.codestates.back.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.codestates.back.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;



import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.BDDMockito.given;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class UserApiTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper mapper;
    @Autowired
    private Gson gson;

    @DisplayName("회원가입 시 정상적으로 유저 정보가 저장되는지 테스트")
    @Test
    public void postUserTest() throws Exception {
        // given
        UserDto.Post post = new UserDto.Post("test@gmail.com", "test1234", "testDisplayName");

        String content = gson.toJson(post);

        given(mapper.userPostToUser(Mockito.any(UserDto.Post.class)))
                .willReturn(new User());

        User mockResultUser = new User();
        mockResultUser.setUserId(1L);
        given(userService.signUpUser(Mockito.any(User.class)))
                .willReturn(mockResultUser);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/users/signup")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        actions
                .andExpect(status().isSeeOther())
                .andDo(document("user-signup",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("displayName").type(JsonFieldType.STRING).description("디스플레이 네임")
                                )
                        )
                ));
    }

    @DisplayName("회원정보 수정 테스트")
    @Test
    public void patchUserTest() throws Exception {
        // given

        UserDto.Post post = new UserDto.Post("updatetest@gmail.com", "test1234", "TestName");

        userService.signUpUser(mapper.userPostToUser(post));

        long userId = 2L;
        UserDto.Update passwordUpdate = new UserDto.Update(userId, "5678test", "AnotherName");

        String content = gson.toJson(passwordUpdate);

        UserDto.Response response = new UserDto.Response(2L,
                "updatetest@gmail.com", "5678test", "AnotherName");

        given(mapper.userPatchToUser(Mockito.any(UserDto.Update.class))).willReturn(new User());

        given(userService.updateUser(Mockito.any(User.class))).willReturn(new User());

        given(mapper.userToUserResponse(Mockito.any(User.class))).willReturn(response);


        // when
        ResultActions actions = mockMvc.perform(
                patch("/users/edit/{userId}", userId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("edit-user",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("userId").description("회원식별자(numberId)")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("회원 식별자").ignored(),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호").optional(),
                                        fieldWithPath("displayName").type(JsonFieldType.STRING).description("Display Name").optional()
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("data.password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("Display Name")
                                )
                        )
                        ));
    }
}
