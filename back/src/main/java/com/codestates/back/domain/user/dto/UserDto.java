package com.codestates.back.domain.user.dto;

import lombok.*;


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
    @Setter
    @AllArgsConstructor
    public static class Update {

        @NotBlank
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
                message = "비밀번호는 영문 및 숫자 조합으로 8자리 이상이어야 합니다.")
        private String password;

        @NotBlank
        private String displayName;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Response {
        private long userId;
        private String email;
        private String displayName;
        private String message;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class UserResponse {
        private long userId;
        private String email;
        private String displayName;
    }


    @Getter
    @Builder
    public static class MyPage {
        private String displayName;
        private Long totalQuestions;
        private Long totalAnswers;
    }

    @Getter
    @Setter
    public static class LoginResponse {
        private String accessToken;
        private Long userId;
        private String displayName;
        private String email;
        private String message;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserProfileResponse {
        private UserDto.Response userProfile;
        private UserDto.MyPage myPage;
    }


}
