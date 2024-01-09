package com.mysite.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration /* 스프링의 환경 설정 파일임을 의미하는 애너테이션 */
@EnableWebSecurity /* 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 애너테이션, 스프링 시큐리티를 활성화하는 역할 */
public class SecurityConfig {
    @Bean /* 스프링 시큐리티의 세부 설정은 @Bean 통해 SecurityFilterChain 빈을 생성하여 설정 */
    /* SecurityFilterChain클래스: 모든 요청 URL에 이 클래스가 필터로 적용되어 URL별로 특별한 설정을 할 수 있게 된다. */
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /* 세부 설정 시작 */
        http
                /* 인증되지 않은 모든 페이지의 요청을 허락, 로그인X -> 모든 페이지 접근 가능 */
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())

                /* CSRF 처리 시 H2 콘솔은 예외로 처리 */
            .csrf((csrf) -> csrf
                .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))

                /* X-Frame-Options 헤더를 DENY 대신 SAMEORIGIN으로 설정
                * -> 프레임에 포함된 웹 페이지가 동일한 사이트에서 제공할 때에만 사용이 허락 */
            .headers((headers) -> headers
                .addHeaderWriter(new XFrameOptionsHeaderWriter(
                    XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))

                /* 스프링 시큐리티의 로그인 설정을 담당 */
            .formLogin((formLogin) -> formLogin
                .loginPage("/user/login") /* 로그인 페이지 url 설정 */
                .defaultSuccessUrl("/")) /* 로그인 성공 시 이동할 페이지 */

                /* 로그아웃 구현 */
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/") /* 로그아웃 성공 시, 루트 페이지로 이동 */
                .invalidateHttpSession(true)) /* 로그아웃 시, 생성된 사용자 세션 삭제 */
        ;
        return http.build();
    }
    
    @Bean /* PasswordEncoder 빈 만들기, 유지보수 위함 */
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); /* BCryptPasswordEncoder 객체를 빈으로 등록해서 사용 */
    }

    /* 스프링 시큐리티의 인증을 처리
    UserSecurityService와 PasswordEncoder를 내부적으로 사용하여 인증과 권한 부여 프로세스를 처리 */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}