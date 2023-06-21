package com.codestates.back.domain.user.controller;


import com.codestates.back.domain.user.dto.UserDto;
import com.codestates.back.domain.user.entity.User;
import com.codestates.back.domain.user.mapper.UserMapper;
import com.codestates.back.domain.user.service.UserService;
import com.codestates.back.global.auth.dto.LoginDto;
import com.codestates.back.global.auth.jwt.JwtTokenizer;
import com.codestates.back.global.dto.SingleResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/users") // User 도메인 주소는 우선 /users로
@Validated
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;
    @Autowired
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;


    public UserController(UserService userService, UserMapper mapper, AuthenticationManager authenticationManager, JwtTokenizer jwtTokenizer) {
        this.userService = userService;
        this.mapper = mapper;
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity postUser(@Valid @RequestBody UserDto.Post requestBody) {
        User user = mapper.userPostToUser(requestBody);

        User createdUser = userService.signUpUser(user);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/login").build().toUri();

        return ResponseEntity.created(location).build();
    }

    // 로그인 (로그인 성공 시 메인페이지로 리디렉션. 헤더에 LOCATION 담아 보냄)

    @PostMapping("/login")
    public ResponseEntity<UserDto.LoginResponse> login(@RequestBody LoginDto loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            Date accessTokenExpiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
            Map<String, Object> claims = new HashMap<>();
            claims.put("email", loginRequest.getEmail());
            String accessToken = jwtTokenizer.generateAccessToken(claims, loginRequest.getEmail(), accessTokenExpiration, jwtTokenizer.getSecretKey());

            UserDto.LoginResponse loginResponse = new UserDto.LoginResponse();
            loginResponse.setAccessToken(accessToken);

            URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/").build().toUri();

            return ResponseEntity.ok()
                    .header(HttpHeaders.LOCATION, location.toString())
                    .body(loginResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



    // 회원정보 수정. URI 주소는 우선 실제 웹과 똑같음
    @PatchMapping("/edit/{userId}")
    public ResponseEntity patchUser(@PathVariable long userId, Authentication authentication,
                                    @Valid @RequestBody UserDto.Update requestBody) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();


        if (!userDetails.getUsername().equals(String.valueOf(userId))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        requestBody.setUserId(userId);

        User user = userService.updateUser(mapper.userPatchToUser(requestBody));

        return new ResponseEntity<>(new SingleResponseDto<>(mapper.userToUserResponse(user)), HttpStatus.OK);
    }

    // 마이페이지
    @GetMapping("/{userId}")
    public ResponseEntity getUser(@PathVariable long userId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!userDetails.getUsername().equals(String.valueOf(userId))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userService.findUser(userId);
        UserDto.MyPage myPage = userService.showMyPage(userId); // Utilize the showMyPage method

        // Create a response DTO containing user profile and myPage information
        UserDto.UserProfileResponse userProfileResponse = new UserDto.UserProfileResponse();
        userProfileResponse.setUserProfile(
                UserDto.Response.builder()
                        .userId(user.getUserId())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .displayName(user.getDisplayName())
                        .build()
        );
        userProfileResponse.setMyPage(
                UserDto.MyPage.builder()
                        .displayName(user.getDisplayName())
                        .totalQuestions(myPage.getTotalQuestions())
                        .totalAnswers(myPage.getTotalAnswers())
                        .build()
        );

        return ResponseEntity.ok(userProfileResponse);
    }



    // 회원탈퇴
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity deleteUser(@PathVariable long userId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!userDetails.getUsername().equals(String.valueOf(userId))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.deleteUser(userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}