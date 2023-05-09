package com.example.miniproject.controller;

import com.example.miniproject.dto.LoginRequestDto;
import com.example.miniproject.dto.MsgAndHttpStatusDto;
import com.example.miniproject.dto.SignupRequestDto;
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

    @PostMapping("//api/user/login")
    public ResponseEntity<MsgAndHttpStatusDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return ResponseEntity.ok(new MsgAndHttpStatusDto("로그인 성공", HttpStatus.OK.value()));
    }

    @GetMapping("/api/user/logout/")
    public ResponseEntity<MsgAndHttpStatusDto> logout(HttpServletRequest request) {

        userService.logout(request);
        return ResponseEntity.ok(new MsgAndHttpStatusDto("로그아웃 완료!", HttpStatus.OK.value()));
    }

    //아이디 중복확인
    @GetMapping("/api/user/signup/id")
    public ResponseEntity<MsgAndHttpStatusDto> checkId (@Valid @RequestParam String userId) {
        userService.checkId(userId);
        return ResponseEntity.ok(new MsgAndHttpStatusDto("사용가능한 아이디 입니다.",HttpStatus.OK.value()));
    }

    //회원가입
    @PostMapping("/api/user/signup")
    public ResponseEntity<MsgAndHttpStatusDto> signup(@Valid @RequestBody SignupRequestDto signUpRequestDto) {
        userService.signup(signUpRequestDto);
        return ResponseEntity.ok(new MsgAndHttpStatusDto("회원가입 성공", HttpStatus.OK.value()));
    }

    @GetMapping("/user/kakaologin/{jwt}")
    public ResponseEntity<?> login_success(@PathVariable String jwt, HttpServletResponse response) throws Exception {

        System.out.println("카카오 로그인 성공 컨트롤러 진입");
        // 로그인 시도
        response.addHeader("Authorization", "Bearer " + jwt);

        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS));


    }
}
