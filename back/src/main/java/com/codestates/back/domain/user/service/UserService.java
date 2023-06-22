package com.codestates.back.domain.user.service;

import com.codestates.back.domain.answer.entity.Answer;
import com.codestates.back.domain.question.domain.Question;
import com.codestates.back.domain.user.dto.UserDto;
import com.codestates.back.domain.user.entity.User;
import com.codestates.back.domain.user.repository.UserRepository;
import com.codestates.back.global.auth.userdetails.CustomUserDetails;
import com.codestates.back.global.auth.userdetails.CustomUserDetailsService;
import com.codestates.back.global.auth.utils.CustomAuthorityUtils;
import com.codestates.back.global.exception.BusinessLogicException;
import com.codestates.back.global.exception.exceptioncode.ExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CustomAuthorityUtils authorityUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityUtils = authorityUtils;
    }

    public User signUpUser(User user) { // 회원가입 로직
        verifyExistsEmail(user.getEmail());

        // 비밀번호 암호화 추가
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        //

        User savedUser = userRepository.save(user);

        if (savedUser == null) {
            throw new IllegalStateException("Failed to save user during signup.");
        }

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
                .ifPresent(password -> findUser.setPassword(passwordEncoder.encode(password)));

        return userRepository.save(findUser);
    }


    public User findUser(long userId) {
        return findVerifiedUser(userId);
    }

    // 로그인한 유저의 정보 가져오기.
    // 현재 토큰을 가지고 있는(로그인한) 유저의 엔티티 (User)를 리턴함.
    public User getLoginUser() {
        return getUserByToken();
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
        User findUser = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        return findUser;
    }

    private void verifyExistsEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.USER_EXISTS);
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


    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User getUserByToken() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUserDetails principalDetails = (CustomUserDetails) principal;

        return principalDetails.getUser();
    }
}
