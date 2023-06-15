package com.codestates.back.domain.question.domain;

import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

//@Component
@Entity
public class QuestionV1 implements Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Column
    private String title;

    @Column
    private String body;

    @Column
    private Date createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionV1 that = (QuestionV1) o;
        return Objects.equals(questionId, that.questionId) && Objects.equals(title, that.title) && Objects.equals(body, that.body) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, title, body, createdAt);
    }
}
