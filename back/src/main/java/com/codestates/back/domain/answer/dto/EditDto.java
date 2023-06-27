package com.codestates.back.domain.answer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EditDto {
    public EditDto (String body) {
        this.body = body;
    }

    String title;
    String body;
}
