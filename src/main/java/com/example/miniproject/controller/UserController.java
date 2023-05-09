package com.example.miniproject.controller;

import com.example.miniproject.dto.LoginRequestDto;
import com.example.miniproject.dto.MsgAndHttpStatusDto;
import com.example.miniproject.dto.SignupRequestDto;
import com.example.miniproject.dto.UserIdRequestDto;
import com.example.miniproject.dto.http.DefaultDataRes;
import com.example.miniproject.dto.http.DefaultRes;
import com.example.miniproject.dto.http.ResponseMessage;
import com.example.miniproject.dto.http.StatusCode;
import com.example.miniproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS));
    }

    @GetMapping("/user/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.LOGOUT_SUCCESS));
    }

    //아이디 중복확인
    @PostMapping("/user/signup/id")
    public ResponseEntity<?> checkId(@Valid @RequestBody UserIdRequestDto userIdRequestDto) {
        userService.checkId(userIdRequestDto);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.Available_ID));
    }

    //회원가입
    @PostMapping("/user/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto signUpRequestDto) {
        userService.signup(signUpRequestDto);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_USER));
    }

    @GetMapping("/user/kakaologin/{jwt}")
    public ResponseEntity<?> login_success(@PathVariable String jwt, HttpServletResponse response) throws Exception {

        System.out.println("카카오 로그인 성공 컨트롤러 진입");

        // 로그인 시도
        response.addHeader("Authorization", "Bearer " + jwt);

        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS));


    }
}
