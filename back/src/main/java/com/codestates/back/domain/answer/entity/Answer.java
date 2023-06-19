package com.codestates.back.domain.answer.entity;

<<<<<<< Updated upstream
import com.codestates.back.domain.question.domain.QuestionV1;
import com.codestates.back.domain.user.entity.User;
import com.codestates.back.global.audit.TimeTracker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

=======
import com.codestates.back.domain.question.entity.Question;
import com.codestates.back.domain.user.entity.User;
import com.codestates.back.global.audit.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
>>>>>>> Stashed changes

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ANSWERS")
<<<<<<< Updated upstream
public class Answer extends TimeTracker {
=======
public class Answer extends Auditable {
>>>>>>> Stashed changes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "STATUS")
    private AnswerStatus answerStatus = AnswerStatus.ANSWER_EXIST;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public void addUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "QUESTION_ID")
<<<<<<< Updated upstream
    private QuestionV1 question;

    public void addQuestion(QuestionV1 question) {
=======
    private Question question;

    public void addQuestion(Question question) {
>>>>>>> Stashed changes

        this.question = question;
    }

    public enum AnswerStatus {
        ANSWER_NOT_EXIST("존재하지 않는 답변"),
        ANSWER_EXIST("존재하는 답변");

        @Getter
        private String status;

        AnswerStatus(String status) {
            this.status = status;
        }
    }



<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes
