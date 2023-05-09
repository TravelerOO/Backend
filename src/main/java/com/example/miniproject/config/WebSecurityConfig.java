package com.example.miniproject.config;

import com.example.miniproject.config.jwt.JwtAuthenticationFilter;
import com.example.miniproject.config.jwt.JwtUtil;
import com.example.miniproject.config.oauth.CustomAuthenticationFailureHandler;
import com.example.miniproject.config.oauth.OAuth2AuthenticationSuccessHandler;
import com.example.miniproject.config.oauth.UserOAuth2Service;
import com.example.miniproject.config.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/user/**",
            "/member/authenticate",
            "/auth/**",
            "/oauth2/authorization/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/board/list"
    };
    private final JwtUtil jwtUtil;
    private final UserOAuth2Service userOAuth2Service;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용 및 resources 접근 허용 설정
        return (web) -> web.ignoring()
//                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF 설정
        http.csrf().disable();
        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests(authorize -> authorize
                        .shouldFilterAllDispatcherTypes(false)
                        .antMatchers(AUTH_WHITELIST)
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                // JWT 인증/인가를 사용하기 위한 설정
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                // 소셜로그인 사용하기
                .oauth2Login()
                .defaultSuccessUrl("/main") // ouauth 로그인이 성공했을시에 이동하게되는 url
                .successHandler(oAuth2AuthenticationSuccessHandler).failureHandler(customAuthenticationFailureHandler) // 인증 프로세서에 따라 사용자 정의로직을 실행
                .userInfoEndpoint()
                .userService(userOAuth2Service); // 로그인이 성공하면 해당 유저의 정보를 들고 userOAuth2Service에서 후처리를 해주겠다는의미.

        // 401 에러 핸들링
        http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);

        http.formLogin()
                .failureHandler(customAuthenticationFailureHandler);

        return http.build();
    }

    @Bean // 비밀번호 암호화 기능 등록
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
