package com.example.miniproject.config.oauth;

import com.example.miniproject.entity.User;
import com.example.miniproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserOAuth2Service extends DefaultOAuth2UserService {

    // OAuth2UserService 클래스는 사용자의 정보들을 기반으로 가입 및 정보수정, 세션 저장등의 기능을 지원해준다.
    //DefaultOAuth2UserService 클래스 안의 loadUser 메서드를 호출되게 했고, 이걸 활용해서 회원가입 작업을 한다.
    //UserOAuth2Service의 loadUser(OAuth2UserRequest oAuth2UserRequest) 메서드는 사용자 정보를 요청할 수 있는 access token 을 얻고나서 실행된다.

    /*
    - access token을 이용해 서드파티 서버로부터 사용자 정보를 받아온다.

    - 해당 사용자가 이미 회원가입 되어있는 사용자인지 확인한다.
    만약 회원가입이 되어있지 않다면, 회원가입 처리한다.
    만약 회원가입이 되어있다면 가입한 적 있다는 log를 찍고 로그인한다. (추후 가입한 적 있다는 alret 추가로 구현하면 좋을듯)

    - UserPrincipal 을 return 한다. 세션 방식에서는 여기서 return한 객체가 시큐리티 세션에 저장된다.
        하지만 JWT 방식에서는 저장하지 않는다. (JWT 방식에서는 인증&인가 수행시 HttpSession을 사용하지 않을 것이다.)
     */
    private final HttpSession httpSession;

    private final UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("로드유저 호출");

        // 로그인에 성공한 사용자의 기본정보를 가지고 온다.
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 사용자의 추가정보를 가지고 온다.
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        String email = (String) kakao_account.get("email");

        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        String nickname = (String) properties.get("nickname");


        /*
        가입한 유저인지 아닌지 체크하는 과정 가지기
        가입을 안한 유저이면 해당 정보들을 바탕으로 회원가입 진행하기
        가입을 한 유저라면 로그인하기
         */


        if(userRepository.findByUserId(nickname).isEmpty()){
            System.out.println("카카오 서버에서 받아온 정보를 기반으로 회원가입을 진행합니다. ");

            User user = new User(nickname, nickname);

            userRepository.save(user);
        }else {
            System.out.println("가입한적 있음.");
        }

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER")), attributes, "id");
    }


}
