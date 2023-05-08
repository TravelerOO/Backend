package com.example.miniproject.config.jwt;

import com.example.miniproject.dto.MsgAndHttpStatusDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtil.resolveAccessToken(request);
        String refreshToken = jwtUtil.resolveRefreshToken(request);

        if (refreshToken != null && accessToken != null) {

            if (jwtUtil.existsRefreshToken(refreshToken) && jwtUtil.validateToken(refreshToken, jwtUtil.getRefreshKey())) {
                if (jwtUtil.getExpiration(jwtUtil.getRefreshKey(), refreshToken) > 0) {
                    if (jwtUtil.validateToken(accessToken, jwtUtil.getAccessKey())) {

                        String userId = jwtUtil.getUserInfoFromToken(accessToken).getSubject();
                        if (jwtUtil.getExpiration(jwtUtil.getAccessKey(), accessToken) < 0) {
                            /// 토큰 발급
                            String newAccessToken = jwtUtil.createAccessToken(userId);
                            /// 헤더에 어세스 토큰 추가
                            jwtUtil.setHeaderAccessToken(response, newAccessToken);
                        }
                        try {
                            this.setAuthentication(userId);
                        } catch (Exception e) {
                            jwtExceptionHandler(response, e.getMessage(), HttpStatus.UNAUTHORIZED.value());
                            return;
                        }
                    } else jwtExceptionHandler(response, "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED.value());
                } else jwtExceptionHandler(response, "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED.value());
            } else
                jwtExceptionHandler(response, "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED.value());
        }

        filterChain.doFilter(request, response);
    }


    public void setAuthentication(String userId) throws UsernameNotFoundException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(userId);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(new MsgAndHttpStatusDto(msg, statusCode));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}

