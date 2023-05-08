package com.example.miniproject.service;

import com.example.miniproject.config.jwt.JwtUtil;
import com.example.miniproject.dto.LoginRequestDto;
import com.example.miniproject.dto.SignupRequestDto;
import com.example.miniproject.dto.UserIdRequestDto;
import com.example.miniproject.entity.User;
import com.example.miniproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final RedisService redisService;

    @Transactional
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String userId = loginRequestDto.getUserId();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("가입하지 않은 회원입니다."));// 예외처리 해주기

        System.out.println(password);
        System.out.println(user.getPassword());

        System.out.println(passwordEncoder.encode(password));
        if (!passwordEncoder.matches(password,user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtil.createAccessToken(user.getUserId());
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId());

        redisService.setValues(refreshToken, user.getUserId());

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
        response.addHeader(JwtUtil.REFRESHTOKEN_HEADER, refreshToken);
    }

    //아이디 중복확인
    @Transactional(readOnly = true)
    public void checkId(String userId) {
        if (userRepository.existsByUserId(userId)) {
            throw new IllegalStateException("이미 사용중인 아이디입니다.");
        }
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        redisService.deleteValues(request.getHeader(JwtUtil.REFRESHTOKEN_HEADER));
    }

    //회원가입
    @Transactional
    public void signup(SignupRequestDto signupRequestDto){
        String userId = signupRequestDto.getUserId();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        String nickname = signupRequestDto.getNickname();
        String name = signupRequestDto.getName();

        Optional<User> found = userRepository.findByUserId(userId);
        if(userRepository.existsByUserId(userId)){
            throw new IllegalStateException("아이디 중복확인을 해주세요.");
        }
        User user = new User(userId,password,nickname,name);
        userRepository.save(user);
    }
}
