package com.example.miniproject.controller;

import com.example.miniproject.dto.LoginRequestDto;
import com.example.miniproject.dto.MsgAndHttpStatusDto;
import com.example.miniproject.dto.SignupRequestDto;
import com.example.miniproject.dto.UserIdRequestDto;
import com.example.miniproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/login")
    public ResponseEntity<MsgAndHttpStatusDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto,response);
        return ResponseEntity.ok(new MsgAndHttpStatusDto("로그인 성공", HttpStatus.OK.value()));
    }

    //아이디 중복확인
    @PostMapping("/user/signup/id")
    public ResponseEntity<MsgAndHttpStatusDto> checkId (@Valid @RequestBody UserIdRequestDto userIdRequestDto){
        userService.checkId(userIdRequestDto);
        return ResponseEntity.ok(new MsgAndHttpStatusDto("사용가능한 아이디 입니다.",HttpStatus.OK.value()));
    }
    //회원가입
    @PostMapping("/user/signup")
    public ResponseEntity<MsgAndHttpStatusDto> signup(@Valid @RequestBody SignupRequestDto signUpRequestDto) {
        userService.signup(signUpRequestDto);
        return ResponseEntity.ok(new MsgAndHttpStatusDto("회원가입 성공",HttpStatus.OK.value()));
    }
}
