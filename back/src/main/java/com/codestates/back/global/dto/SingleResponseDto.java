package com.codestates.back.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SingleResponseDto<T> { // 목록이 아닌 단일 엔티티 Response.
    private T data;
}
