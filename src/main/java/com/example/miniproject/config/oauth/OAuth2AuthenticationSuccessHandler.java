package com.example.miniproject.config.oauth;
import java.io.IOException;
import java.util.Map;


import com.example.miniproject.config.jwt.JwtUtil;
import com.example.miniproject.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    private final RedisService redisService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        System.out.println("oauth 로그인 성공");

//        login 성공한 사용자 목록.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> kakao_account = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
        String email = (String) kakao_account.get("email");
        Map<String, Object> properties = (Map<String, Object>) oAuth2User.getAttributes().get("properties");
        String nickname = (String) properties.get("nickname");

        // jwt 토큰 발급
        String accessToken = jwtUtil.createAccessToken(nickname);
        String refreshToken = jwtUtil.createRefreshToken(nickname);

        String redisKey = refreshToken.substring(7);
        redisService.setValues(redisKey, nickname);

        String url = makeRedirectUrl(accessToken, refreshToken);
        System.out.println("url: " + url);

        if (response.isCommitted()) {
            logger.debug("응답이 이미 커밋된 상태입니다. " + url + "로 리다이렉트하도록 바꿀 수 없습니다.");
            return;
        }
        getRedirectStrategy().sendRedirect(request, response, url);
    }

    // 로그인 성공후에 redirecturl
    private String makeRedirectUrl(String token, String refreshToken) {
        System.out.println("함수호출");

//        return UriComponentsBuilder.fromUriString("http://localhost:8080/main/"+token)
        return UriComponentsBuilder.fromUriString("http://localhost:8080/api/user/kakaologin")
                .queryParam("token", token)
                .queryParam("refreshToken", refreshToken)
                .toUriString();
    }
}