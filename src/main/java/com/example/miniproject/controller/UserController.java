package com.example.miniproject.controller;

import com.example.miniproject.dto.BoardResponseDto;
import com.example.miniproject.dto.LoginRequestDto;
import com.example.miniproject.dto.LoginResponseDto;
import com.example.miniproject.dto.SignupRequestDto;
import com.example.miniproject.dto.http.DefaultDataRes;
import com.example.miniproject.dto.http.DefaultRes;
import com.example.miniproject.dto.http.ResponseMessage;
import com.example.miniproject.dto.http.StatusCode;
import com.example.miniproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        LoginResponseDto loginResponseDto = userService.login(loginRequestDto,response);
        return ResponseEntity.ok(new DefaultDataRes<LoginResponseDto>(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, loginResponseDto));
    }


    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseEntity.ok(new DefaultRes<>(StatusCode.OK, ResponseMessage.LOGOUT_SUCCESS));
    }

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto signUpRequestDto) {
        userService.signup(signUpRequestDto);
        return ResponseEntity.ok(new DefaultRes<>(StatusCode.OK, ResponseMessage.CREATED_USER));
    }

    @GetMapping("/kakaologin/{jwt}")
    public ResponseEntity<?> login_success(@PathVariable String jwt, HttpServletResponse response) throws Exception {

        System.out.println("카카오 로그인 성공 컨트롤러 진입");
        // 로그인 시도
        response.addHeader("Authorization", "Bearer " + jwt);
        return ResponseEntity.ok(new DefaultRes<>(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS));
    }
}
