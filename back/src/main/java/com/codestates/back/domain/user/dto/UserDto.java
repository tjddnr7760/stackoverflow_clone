package com.codestates.back.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;



import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserDto {

    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
                message = "비밀번호는 영문 및 숫자 조합으로 8자리 이상이어야 합니다.")
        private String password;

        @NotBlank
        private String displayName;
    }

    @Getter
    @AllArgsConstructor
    public static class Update {
        private long userId;

        @NotBlank
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
                message = "비밀번호는 영문 및 숫자 조합으로 8자리 이상이어야 합니다.")
        private String password;

        @NotBlank
        private String displayName;

        public void setUserId(long userId) {
            this.userId = userId;
        }
    }

    @AllArgsConstructor
    @Getter
    public static class Response {
        private long userId;
        private String email;
        private String password;
    }


    @Getter
    @Builder
    public static class MyPage {
        private String displayName;
        private Long totalQuestions;
        private Long totalAnswers;
    }


}
