package com.jojoldu.book.springboot.config.auth;

import com.jojoldu.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().headers().frameOptions().disable()    // h2-console 화면을 사용하기 위해 해당 옵션들은 disable 처리
                .and()
                .authorizeRequests()    // URL별 권한 관리를 설정하는 옵션의 시작점 / antMatchers옵션을 사용할 수 있게됨.
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll().antMatchers("/api/v1/**").hasRole(Role.USER.name())
                // 권한 관리 대상을 지정하는 옵션.
                // URL, HTTP 메소드별로 관리가 가능
                // "/"등 지정된 URL들은 permitAll()옵션을 통해 전체 열람 권한을 줌.
                // POST 이면서 "/api/v1/**"주소를 가진 API는 USER 권하느을 가진 사람만 가능함.
                .anyRequest()
                //설정된 값들 이외 나머지 URL들을 나타낸다.
                .authenticated()
                // URL들은 모두 인증된 사용자들에게만 허용하게 한다.(인증된 사용자 즉, 로그인 한 사용자들)
                .and()
                .logout()   // 로그아웃 기능에 대한 여러 설정의 시작
                .logoutSuccessUrl("/")  // 성공시 /로 이동
                .and()
                .oauth2Login()  // OAuth2 로그인 기능에 대한 여러 설정의 시작
                .userInfoEndpoint() // OAuth2 로그인 성공 후 사용자 정보를 가져올 때의 설정들을 담당
                .userService(customOAuth2UserService);  // 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록

    }
}
