package com.codestates.back.domain.user.entity;

import com.codestates.back.domain.answer.entity.Answer;
import com.codestates.back.domain.question.entity.Question;
import com.codestates.back.global.audit.TimeTracker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "so_user")
public class User extends TimeTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;

    private String password;

    private String displayName;



    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<>();

    public User(Long userId) {
        this.userId = userId;
    }

}
