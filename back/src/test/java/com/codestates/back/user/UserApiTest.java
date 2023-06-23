package com.codestates.back.user;

import com.codestates.back.domain.user.controller.UserController;
import com.codestates.back.domain.user.dto.UserDto;
import com.codestates.back.domain.user.entity.User;
import com.codestates.back.domain.user.mapper.UserMapper;
import com.codestates.back.domain.user.repository.UserRepository;
import com.codestates.back.domain.user.service.UserService;
import com.codestates.back.global.auth.dto.LoginDto;
import com.codestates.back.global.auth.jwt.JwtTokenizer;
import com.codestates.back.global.auth.userdetails.CustomUserDetails;
import com.codestates.back.global.auth.userdetails.CustomUserDetailsService;
import com.codestates.back.global.auth.utils.CustomAuthorityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.google.gson.Gson;
import org.springframework.test.web.servlet.ResultActions;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.codestates.back.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.codestates.back.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;



import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserApiTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper mapper;
    @Autowired
    private Gson gson;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtTokenizer jwtTokenizer;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CustomAuthorityUtils customAuthorityUtils;


    @DisplayName("회원가입 시 정상적으로 유저 정보가 저장되는지 테스트")
    @Test
    public void postUserTest() throws Exception {
        // given
        UserDto.Post post = new UserDto.Post("test@gmail.com", "test1234", "testDisplayName");

        given(mapper.userPostToUser(Mockito.any(UserDto.Post.class))).willReturn(new User());

        User mockResultUser = new User();
        mockResultUser.setUserId(1L);
        mockResultUser.setEmail("test@gmail.com");
        mockResultUser.setDisplayName("testDisplayName");
        given(userService.signUpUser(Mockito.any(User.class))).willReturn(mockResultUser);

        String content = objectMapper.writeValueAsString(post);

        // when
        ResultActions actions = mockMvc.perform(
                post("/users/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.email", is("test@gmail.com")))
                .andExpect(jsonPath("$.displayName", is("testDisplayName")))
                .andExpect(jsonPath("$.message", is("성공적으로 회원가입 되었습니다.")))
                .andDo(document("User Signup",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("displayName").type(JsonFieldType.STRING).description("디스플레이 네임")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Created user location")
                        ),
                        responseFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("User ID"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("User email"),
                                fieldWithPath("displayName").type(JsonFieldType.STRING).description("User display name"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("Response message")
                        )
                ));
    }

    @DisplayName("로그인 테스트")
    @Test
    public void loginTest() throws Exception {
        // given
        LoginDto loginDto = new LoginDto("test@gmail.com", "test1234");
        User mockUser = new User();
        mockUser.setEmail("test@gmail.com");
        mockUser.setDisplayName("testDisplayName");

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", mockUser.getEmail());
        String subject = mockUser.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        // Mock the behavior of user authentication and token creation
        String mockToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRqZGRucjc3NjBAbmF2ZXIuY29tIiwic3ViIjoidGpkZG5yNzc2MEBuYXZlci5jb20iLCJpYXQiOjE2ODc0NzA0MjQsImV4cCI6MTY4NzU1NjgyNH0.k66dpcHHqI02Ijc1V8fattleaWfwjyolU2-ym-1h5Ks";
        given(jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey)).willReturn(mockToken);

        String content = gson.toJson(loginDto);

        // when
        ResultActions actions = mockMvc.perform(
                post("/users/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(mockToken)))
                .andDo(document("User Login",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("token").type(JsonFieldType.STRING).description("JWT token")
                        )
                ));
    }

    @DisplayName("로그아웃 테스트")
    @Test
    public void logoutTest() {
        // given

        // when

        // then
    }

    @DisplayName("마이페이지 테스트")
    @Test
    public void mypageTest() {
        // given

        // when

        // then
    }

    @DisplayName("회원정보 수정 테스트")
    @Test
    @WithMockUser(username = "updatetest@gmail.com", roles = {"USER"})
    public void patchUserTest() throws Exception {
        // given
        UserDto.Post post = new UserDto.Post("updatetest@gmail.com", "test1234", "TestName");
        User createdUser = userService.signUpUser(mapper.userPostToUser(post));
        long userId = createdUser.getUserId();

        UserDto.Update updatedUserDto = new UserDto.Update("5678test", "AnotherName");
        String content = gson.toJson(updatedUserDto);

        User updatedUser = new User();
        updatedUser.setUserId(userId);
        updatedUser.setEmail("updatetest@gmail.com");
        updatedUser.setPassword("5678test");
        updatedUser.setDisplayName("AnotherName");

        UserDto.Response response = new UserDto.Response(updatedUser.getUserId(), updatedUser.getEmail(), updatedUser.getDisplayName(), "회원정보 수정이 완료되었습니다.");

        given(mapper.userPatchToUser(Mockito.any(UserDto.Update.class))).willReturn(updatedUser);
        given(userService.updateUser(Mockito.any(User.class))).willReturn(updatedUser);
        given(mapper.userToUserResponse(Mockito.any(User.class))).willReturn(response);

        // Set up the authentication context
        CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService(userRepository, customAuthorityUtils);
        CustomUserDetails userDetails = new CustomUserDetails(createdUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, // UserDetails object representing the authenticated user
                null, // Credentials (password) if applicable
                userDetails.getAuthorities() // Authorities/roles of the user
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        mockMvc.perform(patch("/users/edit/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(updatedUser.getUserId()))
                .andExpect(jsonPath("$.data.email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("$.data.displayName").value(updatedUser.getDisplayName()))
                .andExpect(jsonPath("$.message").value(response.getMessage()))
                .andDo(document("Update User",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(parameterWithName("userId").description("회원식별자(numberId)")),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호").optional(),
                                fieldWithPath("displayName").type(JsonFieldType.STRING).description("Display Name").optional()
                        ),
                        responseFields(
                                fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("Display Name"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )
                ));
    }

    @DisplayName("회원정보 삭제 테스트")
    @Test
    public void deleteTest() {
        // given

        // when

        // then
    }
}
