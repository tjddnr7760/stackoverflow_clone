package com.codestates.back.domain.answer.entity;

import com.codestates.back.domain.answer.dto.AnswerDto;
import com.codestates.back.domain.question.domain.Question;
import com.codestates.back.domain.user.entity.User;
import com.codestates.back.global.audit.TimeTracker;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "answer")
@Getter
public class Answer extends TimeTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    Question question;

    protected Answer() {
    }

    public Answer(String body) {
        this.body = body;
    }

    public Answer update(AnswerDto answerDto) {
        this.body = answerDto.getBody();
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}