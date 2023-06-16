package com.codestates.back.domain.user.service;

import com.codestates.back.domain.answer.entity.Answer;
import com.codestates.back.domain.question.entity.Question;
import com.codestates.back.domain.user.dto.UserDto;
import com.codestates.back.domain.user.entity.User;
import com.codestates.back.domain.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User signUpUser(User user) { // 회원가입 로직
        verifyExistsEmail(user.getEmail());
        User savedUser = userRepository.save(user);

        return savedUser;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public User updateUser(User user) {
        User findUser = findVerifiedUser(user.getUserId());

        // Display Name 수정 로직. 만약 존재하는 유저이고 새 Display Name이 입력됐다면
        // 이름을 수정한다.
        Optional.ofNullable(user.getDisplayName())
                .ifPresent(displayName -> findUser.setDisplayName(displayName));

        // 비밀번호 수정 로직. 만약 존재하는 유저이고 새 비밀번호가 입력됐다면 수정
        Optional.ofNullable(user.getPassword())
                .ifPresent(password -> findUser.setPassword(password));

        return userRepository.save(findUser);
    }

    @Transactional(readOnly = true)
    public User findUser(long userId) {
        return findVerifiedUser(userId);
    }

    public Page<User> findUsers(int page) {
        return userRepository.findAll(PageRequest.of(page, 15,
                Sort.by("userId").descending()));
    }

    public void deleteUser(long userId) {
        User findUser = findVerifiedUser(userId);

        userRepository.delete(findUser);
    }

    @Transactional(readOnly = true)
    public User findVerifiedUser(long userId) {

        Optional<User> optionalUser = userRepository.findById(userId);
        User findUser = optionalUser.get();
                // new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        // 아직 유저를 찾을 수 없는 경우의 예외 처리 구현전임

        return findUser;
    }

    private void verifyExistsEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        /* 유저 이메일 없는 경우 예외처리 구현전임
        if (user.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.USER_EXISTS);
        }
        */

        if (user.isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
    }

    public UserDto.MyPage showMyPage(long userId) {
        User user = findVerifiedUser(userId);

        List<Question> questionsList = user.getQuestions();
        List<Answer> answersList = user.getAnswers();

        long totalQuestions = questionsList.size();
        long totalAnswers = answersList.size();

        UserDto.MyPage myPage = UserDto.MyPage.builder()
                .displayName(user.getDisplayName())
                .totalQuestions(totalQuestions)
                .totalAnswers(totalAnswers)
                .build();

        return myPage;
    }

}
