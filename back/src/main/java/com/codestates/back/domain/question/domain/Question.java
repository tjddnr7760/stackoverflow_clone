package com.codestates.back.domain.question.domain;

import com.codestates.back.domain.question.controller.dto.QuestionDto;
import com.codestates.back.domain.user.entity.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

//@Component
@Getter
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
    private LocalDateTime createdAt = LocalDateTime.now();

    protected QuestionV1() {
    }

    public QuestionV1(String title, String body) {
        this.title = title;
        this.body = body;
    }

    @Override
    public void update(QuestionDto questionDto) {
        this.title = questionDto.getTitle();
        this.body = questionDto.getBody();
        this.createdAt = LocalDateTime.now();
    }

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;
}
