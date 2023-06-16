package com.codestates.back.domain.user.controller;


import com.codestates.back.domain.user.dto.UserDto;
import com.codestates.back.domain.user.entity.User;
import com.codestates.back.domain.user.mapper.UserMapper;
import com.codestates.back.domain.user.service.UserService;
import com.codestates.back.global.dto.SingleResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/users") // User 도메인 주소는 우선 /users로
@Validated
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    public UserController(UserService userService, UserMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity postUser(@Valid @RequestBody UserDto.Post requestBody) {
        User user = mapper.userPostToUser(requestBody);


        User createdUser = userService.signUpUser(user);

        return new ResponseEntity<>(new SingleResponseDto<>("회원가입이 성공적으로 완료되었습니다."), HttpStatus.CREATED);
    }

    // 회원정보 수정. URI 주소는 우선 실제 웹과 똑같음
    @PatchMapping("/edit/{user-id}")
    public ResponseEntity patchUser(@PathVariable long userId,
                                    @Valid @RequestBody UserDto.Update requestbody) {
        requestbody.setUserId(userId);

        User user = userService.updateUser(mapper.userPatchToUser(requestbody));

        return new ResponseEntity<>(new SingleResponseDto<>(mapper.userToUserResponse(user)), HttpStatus.OK);
    }

    // 단일 유저정보 조회(마이페이지 아님). 마이페지이를 구현 못해서 일단 개별 회원조회로 만들었습니다.
    @GetMapping("/{user-id}")
    public ResponseEntity getUser(@PathVariable long userId) {
        User user = userService.findUser(userId);

        return new ResponseEntity<>(new SingleResponseDto<>(mapper.userToUserResponse(user)), HttpStatus.OK);
    }



    // 회원탈퇴
    @DeleteMapping("/delete/{user-id}")
    public ResponseEntity deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /* 마이페이지(내가 쓴글 및 정보)
    @GetMapping
    public ResponseEntity mypage() {

    }
    */
}
