package com.example.miniproject.service;

import com.example.miniproject.config.jwt.JwtUtil;
import com.example.miniproject.dto.LoginRequestDto;
import com.example.miniproject.dto.MsgAndHttpStatusDto;
import com.example.miniproject.dto.SignupRequestDto;
import com.example.miniproject.dto.http.ResponseMessage;
import com.example.miniproject.dto.http.StatusCode;
import com.example.miniproject.entity.RefreshToken;
import com.example.miniproject.dto.http.StatusCode;
import com.example.miniproject.entity.User;
import com.example.miniproject.exception.CustomException;
import com.example.miniproject.repository.TokenRepository;
import com.example.miniproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final TokenRepository tokenRepository;
    private final RedisService redisService;

    @Transactional
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String userId = loginRequestDto.getUserId();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(ResponseMessage.NOT_FOUND_USER, StatusCode.UNAUTHORIZED));// 예외처리 해주기

        System.out.println(password);
        System.out.println(user.getPassword());

        System.out.println(passwordEncoder.encode(password));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ResponseMessage.NOT_FOUND_USER, StatusCode.UNAUTHORIZED);
        }

        String accessToken = jwtUtil.createAccessToken(user.getUserId());
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId());

        redisService.setValues(refreshToken, user.getUserId());
        RefreshToken refreshTokenEntity = new RefreshToken(refreshToken.substring(7));
        tokenRepository.save(refreshTokenEntity);

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
        response.addHeader(JwtUtil.REFRESHTOKEN_HEADER, refreshToken);
    }

    //아이디 중복확인
    @Transactional(readOnly = true)
    public void checkId(String userId) {
        if (userRepository.existsByUserId(userId)) {
            throw new CustomException(ResponseMessage.ALREADY_ENROLLED_USER, StatusCode.ID_DUPLICATE);
        }
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        if (request.getHeader(JwtUtil.REFRESHTOKEN_HEADER) != null && redisService.getValues(request.getHeader(JwtUtil.REFRESHTOKEN_HEADER)) != null) {
            redisService.deleteValues(request.getHeader(JwtUtil.REFRESHTOKEN_HEADER));
            tokenRepository.deleteByRefreshToken(request.getHeader(JwtUtil.REFRESHTOKEN_HEADER).substring(7));
        } else {
            throw new CustomException(ResponseMessage.WRONG_ACCESS, StatusCode.BAD_REQUEST);
        }
    }

    //회원가입
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        // 지훈님 수정 후 exception customizing 필요
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
