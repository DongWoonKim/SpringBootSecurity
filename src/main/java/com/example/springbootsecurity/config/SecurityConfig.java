package com.example.springbootsecurity.config;

import com.example.springbootsecurity.security.filter.FormLoginFilter;
import com.example.springbootsecurity.security.handler.FormLoginAuthenticationFailureHandler;
import com.example.springbootsecurity.security.handler.FormLoginAuthenticationSuccessHandler;
import com.example.springbootsecurity.security.jwt.HeaderTokenExtractor;
import com.example.springbootsecurity.security.provider.FormLoginAuthenticationProvider;
import com.example.springbootsecurity.security.provider.JWTAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
빈을 ApplicationContext 에 등록하기만 하면 스프링이 자동으로 생성해주고 요청을 가로채서 DelegatingFilterProxy 로 전달해준다.

스프링 프레임워크 3.1에서부터 어노테이션을 통한 자바 설정을 지원하기 때문에 스프링 시큐리티 3.2부터는 XML 로 설정하지 않고도 간단하게 설정할 수 있도록 지원한다.
원래 XML 기반의 설정에서는 web.xml 에 org.springframework.web.filter.DelegatingFilterProxy 라는 springSecurityFilterChain 을 등록하는 것으로 시작한다

자바 기반의 설정에서는 WebSecurityConfigurerAdapter 를 상속받은 클래스에
@EnableWebSecurity 어노테이션을 명시하는 것만으로도 springSecurityFilterChain 가 자동으로 포함되어진다.

 */

/**
 * SecurityConfig
 *
 */
/*
WebSecurityConfigurerAdapter 추상 클래스를 상속 받는다.
-> 스프링 자동 보안 구성을 건너뛰고 사용자정의 보안구성하기 위해서 상속받는 클래스
반대되는 전역을 보안하는 상속 클래스 GlobalAuthenticationConfigurerAdapter 존재한다.
-> 가장 먼저 인증이 필요한 서버에 사용자가 접속시 가장 처음 Filter를 연결해주는 역할
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final FormLoginAuthenticationSuccessHandler formLoginAuthenticationSuccessHandler;
    private final FormLoginAuthenticationFailureHandler formLoginAuthenticationFailureHandler;

    private final FormLoginAuthenticationProvider provider;
    private final JWTAuthenticationProvider jwtProvider;
    private final HeaderTokenExtractor headerTokenExtractor;

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    protected FormLoginFilter formLoginFilter() throws Exception {

        FormLoginFilter filter = new FormLoginFilter(
                "/api/account/login",
                formLoginAuthenticationSuccessHandler,
                formLoginAuthenticationFailureHandler
        );

        filter.setAuthenticationManager(super.authenticationManager());

        return filter;
    }

    /**
     * configure 메소드는 인증을 담당할 프로바이더 구현체를 설정, 필터 등록을 하는 메소드이다.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(this.provider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // spring security 초기값 인증을 풀어줄 수 있도록 설정
        http
                .csrf()
                .disable();
        http
                .headers()
                .frameOptions()
                .disable();

        http
                .addFilterBefore(
                        formLoginFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );

    }
}
