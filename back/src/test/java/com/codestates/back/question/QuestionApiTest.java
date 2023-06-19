package com.codestates.back.question;

import com.codestates.back.domain.user.controller.UserController;
import com.codestates.back.domain.user.mapper.UserMapper;
import com.codestates.back.domain.user.service.UserService;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class QuestionApiTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper mapper;
    @Autowired
    private Gson gson;

    @DisplayName("임시 테스트1") // DisplayName은 필수는 아닙니다. 원하는대로 변경하시거나 지우셔도 됩니다.
    @Test
    public void test1() {
        // test 로직
    }

    @DisplayName("임시 테스트2")
    @Test
    public void test2() {
        // test 로직
    }


    // ..... 이어서 필요한 만큼 테스트 메서드 작성해주시면 됩니다.
    // api 문서 생성 주소는 back/build/generated-snippets/각 메서드 입니다.
    // 테스트 작성법은 유어클래스 참고해주세요. https://urclass.codestates.com/content/ce8c47ce-a95c-48b3-8428-b64d04496c7d?playlist=2370
    // 코드 중에서도 andDo(document ~~ 부분입니다.
}
