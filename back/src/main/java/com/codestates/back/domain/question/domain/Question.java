package com.codestates.back.domain.question.domain;

import com.codestates.back.domain.answer.entity.Answer;
import com.codestates.back.domain.question.controller.dto.QuestionDto;
import com.codestates.back.domain.user.entity.User;
import com.codestates.back.global.audit.TimeTracker;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "question")
public class Question extends TimeTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    private String title;

    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Question() {
    }

    public Question(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public void update(QuestionDto questionDto) {
        this.title = questionDto.getTitle();
        this.body = questionDto.getBody();
    }

    public void setUser(User user) {
        this.user = user;
        // 양방향 매핑 해줘야함. user에서도 question 추가해줘야함.
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
