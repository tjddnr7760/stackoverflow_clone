package com.codestates.back.global.exception.exceptioncode;

import lombok.Getter;

public enum ExceptionCode {
    USER_NOT_FOUND(404, "해당 사용자 정보가 없습니다."),
    USER_EXISTS(409, "이미 사용중인 이메일입니다."),
    USER_NOT_LOGGED_IN(401, "마이페이지에는 로그인해야 접근할 수 있습니다.");

    // 여기에 각 도메인에서 필요한 ExceptionCode를 추가하여 사용하세요.


    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
