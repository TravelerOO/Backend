package com.example.miniproject.service;

import com.example.miniproject.config.jwt.JwtUtil;
import com.example.miniproject.dto.LoginRequestDto;
import com.example.miniproject.dto.SignupRequestDto;
import com.example.miniproject.dto.http.ResponseMessage;
import com.example.miniproject.dto.http.StatusCode;
import com.example.miniproject.entity.User;
import com.example.miniproject.exception.CustomException;
import com.example.miniproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
@Slf4j
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
                () -> new CustomException(ResponseMessage.NOT_FOUND_USER, StatusCode.UNAUTHORIZED));// 예외처리 해주기

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ResponseMessage.NOT_FOUND_USER, StatusCode.UNAUTHORIZED);
        }

        String accessToken = jwtUtil.createAccessToken(user.getUserId());
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId());

        String redisKey = refreshToken.substring(7);
        redisService.setValues(redisKey, user.getUserId());

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
        response.addHeader(JwtUtil.REFRESHTOKEN_HEADER, refreshToken);
    }

    @Transactional(readOnly = true)
    public void logout(HttpServletRequest request) {
        String refreshToken = request.getHeader(JwtUtil.REFRESHTOKEN_HEADER).substring(7);

        if (refreshToken != null && redisService.getValues(refreshToken) != null) {
            redisService.deleteValues(refreshToken);
        } else {
            throw new CustomException(ResponseMessage.WRONG_ACCESS, StatusCode.BAD_REQUEST);
        }
    }

    //회원가입
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {

        String userId = signupRequestDto.getUserId();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        String nickname = signupRequestDto.getNickname();

        if (userRepository.existsByUserId(userId)) {
            throw new CustomException(ResponseMessage.ALREADY_ENROLLED_USER, StatusCode.ID_DUPLICATE);
        }

        User user = new User(userId, password, nickname);
        userRepository.save(user);
    }
}
