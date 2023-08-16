package movierankchart.security.config;

import lombok.RequiredArgsConstructor;
import movierankchart.security.filter.JwtFilter;
import movierankchart.security.handler.CustomAuthenticationSuccessHandler;
import movierankchart.security.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors();  // CORS 관련 헤더를 응답에 추가
        http.csrf()
                .disable();  // CSRF 비활성화
        // UsernamePasswordAuthenticationFilter 전에 직접 만든 jwtFilter 추가
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests((authorize) -> authorize  //
                .antMatchers("/v1/login")
                .authenticated()  // 인증이 필요한 경로 지정
                .anyRequest()
                .permitAll()  // 그외 경로는 접근 허용
        );
        http.oauth2Login()  // OAuth2 기반의 로그인
                .successHandler(customAuthenticationSuccessHandler)  // 인증 성공 시 실행할 핸들러 지정
                .userInfoEndpoint()
                .userService(customOAuth2UserService);  // OAuth2User를 로드하는 로직이 구현된 OAuth2UserService 설정
        return http.build();
    }
}


