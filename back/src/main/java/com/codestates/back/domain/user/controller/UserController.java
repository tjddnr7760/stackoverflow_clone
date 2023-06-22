package com.codestates.back.domain.user.controller;


import com.codestates.back.domain.user.dto.UserDto;
import com.codestates.back.domain.user.entity.User;
import com.codestates.back.domain.user.mapper.UserMapper;
import com.codestates.back.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    @Autowired
    public UserController(UserService userService, UserMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> postUser(@Valid @RequestBody UserDto.Post requestBody) {
        User user = mapper.userPostToUser(requestBody);

        User createdUser = userService.signUpUser(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/users/login")
                .build()
                .toUri();

        UserDto.Response response = UserDto.Response.builder()
                .userId(createdUser.getUserId())
                .email(createdUser.getEmail())
                .displayName(createdUser.getDisplayName())
                .message("성공적으로 회원가입 되었습니다.")
                .build();

        return ResponseEntity.created(location).body(response);

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "로그아웃 되었습니다. 토큰을 파괴해 주세요");

        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/mypage")
    public ResponseEntity getMyPage(Authentication authentication) {
        String email = authentication.getName();
        User user1 = userService.findUserByEmail(email);

        if (user1 != null) {
            long userId = user1.getUserId();

            User user = userService.findUser(userId);
            UserDto.MyPage myPage = userService.showMyPage(userId);
            UserDto.Response userProfile = new UserDto.Response(
                    userId,
                    user.getEmail(),
                    user.getDisplayName(),
                    "마이페이지"
            );

            UserDto.UserProfileResponse response = UserDto.UserProfileResponse.builder()
                    .userProfile(userProfile)
                    .myPage(myPage)
                    .build();

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/edit")
    public ResponseEntity patchUser(Authentication authentication,
                                    @Valid @RequestBody UserDto.Update requestBody) {
        try {
            String email = authentication.getName();
            User foundUser = userService.findUserByEmail(email);
            foundUser.setDisplayName(requestBody.getDisplayName());
            foundUser.setPassword(requestBody.getPassword());

            User user = userService.updateUser(foundUser);
            return new ResponseEntity<>(mapper.userToUserResponse(user), HttpStatus.OK);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 회원탈퇴 API
    @DeleteMapping("/delete")
    public ResponseEntity deleteUser(Authentication authentication) {
        String email = authentication.getName();

        User user = userService.findUserByEmail(email);
        userService.deleteUser(user.getUserId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}