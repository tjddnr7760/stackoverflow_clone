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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    // 회원가입 API(회원가입 성공시 로그인페이지로
    // URI = "/users/signup"
    // 응답 body에 response를 담음(userId, email, displayName, 회원가입 성공 메시지)
    // 회원가입 성공시 로그인 페이지("/users/login")로 리디렉션하도록 Location을 응답 헤더에 담음.
    @PostMapping("/signup")
    public ResponseEntity<?> postUser(@Valid @RequestBody UserDto.Post requestBody) {
        User user = mapper.userPostToUser(requestBody);

        User createdUser = userService.signUpUser(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()  // This gets the current context path
                .path("/users/login")  // 로그인 페이지 uri
                .build()
                .toUri();

        // Create the response object
        UserDto.Response response = UserDto.Response.builder()
                .userId(createdUser.getUserId())
                .email(createdUser.getEmail())
                .displayName(createdUser.getDisplayName())
                .message("성공적으로 회원가입 되었습니다.")
                .build();

        // Return the response object in the body of the response
        return ResponseEntity.created(location).body(response);

    }

    // 로그인 API(로그인 성공 시 메인페이지로 리디렉션. 헤더에 LOCATION 담아 보냄)
    // Login 요청(LoginDto : 이메일과 패스워드로 구성)이 오면 아래와 같이 처리
    // 이메일:패스워드 짝이 db에 존재하는지 확인
    // 확인 후 존재한다면 액세스 토큰을 사용자에게 부여함.(리프레쉬 토큰은 사용 x)
    // 액세스 토큰 만료는 24시간(중요하진 않음)
    // 액세스 토큰까지 부여가 완료되었다면 액세스 토큰, userId, displayName, email, 로그인 성공 메시지를 담은 response를 응답 바디에 담음.
    // 헤더에는 로그인 완료 후 메인페이지("/")로 리디렉션 되도록 Location을 담음.
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            Date accessTokenExpiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
            Map<String, Object> claims = new HashMap<>();
            claims.put("email", loginRequest.getEmail());


            String base64SecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
            String accessToken = jwtTokenizer.generateAccessToken(claims, loginRequest.getEmail(), accessTokenExpiration, base64SecretKey);

            User user = userService.findUserByEmail(loginRequest.getEmail());

            UserDto.LoginResponse loginResponse = new UserDto.LoginResponse();
            loginResponse.setAccessToken(accessToken);
            loginResponse.setUserId(user.getUserId());
            loginResponse.setDisplayName(user.getDisplayName());
            loginResponse.setEmail(user.getEmail());
            loginResponse.setMessage("로그인에 성공했습니다.");

            URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/").build().toUri();


            return ResponseEntity.ok()
                    .header(HttpHeaders.LOCATION, location.toString())
                    .body(loginResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // 로그아웃 API
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidate the current user's session and clear authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        // 로그아웃은 별도 dto 없고 그냥 메시지 : 로그아웃 되었습니다만 response 바디로
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "로그아웃 되었습니다.");

        // Return the response object
        return ResponseEntity.ok(responseBody);
    }

    // 마이페이지로 이동 API
    // 내 프로필 버튼을 눌렀을 때 마이페이지로 리디렉션됨. 리디렉션에 주의. 바로 마이페이지로 이동하는게 아님.
    // 이유는 유저는 내 userId를 알 필요도 없고 알지도 못함(브라우저 주소창 보면 알 수 있긴함). 허나 유저가 신경쓸 부분이 아님
    // 따라서 @PathVariable로 유저가 직접 자신의 userId를 입력하게 하는 방식은 적절하지 않다고 생각했고
    // 유저 입장에서는 버튼 하나만 누르면 알아서 내 마이페이지(users/{userId})로 이동하는 간접적인 방식이라고 생각함.
    // 아래 코드는 로직상 /users/mypage로 get
    // 저 uri로 get하게 되면 현재 로그인한 사용자인지 확인하고 로그인한 유저의 userId를 찾음.
    // 그리고 /users/{userId}로 리디렉션하게됨. 이 /users/{userId}가 실제 마이페이지 주소가 되는 것임.
    // 응답 바디는 따로 없고 헤더에 리디렉션할 URI(실제 마이페이지 주소)를 Location으로 담음
    @GetMapping("/mypage")
    public ResponseEntity getMyPage(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User user = userService.findUserByEmail(email);

        if (user != null) {
            long userId = user.getUserId();
            // Redirect the user to their profile page
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/" + userId).build().toUri();
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, location.toString())
                    .build();
        } else {
            // User not found, return an appropriate response
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    // 실제 마이페이지 API
    // 위 마이페이지 버튼 클릭(마이페이지로 리디렉션) 후 마이페이지가 실제로 뜨는 메소드임
    // 실제로 마이페이지에 해당하는 /users/{userId}에 GET하는 매소드
    // UserProfileResponse라는 Dto로 응답하게 되는데
    // UserProfileResponse는 다시 response dto와 mypage dto로 구성됨
    // response dto는 말그대로 userId, email, displayName, 리디렉션 메시지를 담은 dto고
    // mypage dto는 displayName(중복이네요 ㅠ), totalQuestions, totalAnswers를 담은 dto입니다.
    // 왜 이렇게 구성했냐고 물의시면.. 하다보니 그렇게 됐네요 죄송합니다. 그러고보니 가입날짜 빼먹은거 같네요
    // 포인트는 MyPage에 접속했을때 내 닉네임과 작성질문 수, 작성답변 수가 뜬다는 겁니다.
    @GetMapping("/{userId}")
    public ResponseEntity getUserProfile(@PathVariable long userId) {
        User user = userService.findUser(userId);

        if (user != null) {
            UserDto.MyPage myPage = userService.showMyPage(userId);
            UserDto.Response userProfile = new UserDto.Response(
                    userId,
                    user.getEmail(),
                    user.getDisplayName(),
                    "마이페이지로 리디렉션 되었습니다."
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

    // 회원정보 수정 API
    // 위 마이페이지와 연결되는 부분임.
    // 마이페이지 이동 api를 통해서 서버측에서 해당하는 유저의 userId를 갖고 있음.
    // 이제 마이페이지에서 edit 버튼울 누르면 /users/edit/{userId}로 Patch를 쏨
    // 마찬가지로 이미 로그인한 유저만을 위한 기능이기 때문에 다시 한번 Authentication 체크가 있음.
    // 로그인한 유저의 이메일과 db 저장된 이메일 일치할 경우 patch 성공
    @PatchMapping("/edit/{userId}")
    public ResponseEntity patchUser(@PathVariable long userId, Authentication authentication,
                                    @Valid @RequestBody UserDto.Update requestBody) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {      // 로그인한 유저인지 체크
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            User foundUser = userService.findUser(userId);


            if (!userDetails.getUsername().equals(foundUser.getEmail())) {          // 로그인한 유저의 정보가 db 정보와 일치하는지 체크
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }


            User user = userService.updateUser(mapper.userPatchToUser(requestBody));

            return new ResponseEntity<>(mapper.userToUserResponse(user), HttpStatus.OK);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    // 회원탈퇴 API
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