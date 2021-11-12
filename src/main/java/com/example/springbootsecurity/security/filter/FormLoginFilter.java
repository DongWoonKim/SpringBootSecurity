package com.example.springbootsecurity.security.filter;

import com.example.springbootsecurity.dto.AccountFormDTO;
import com.example.springbootsecurity.security.token.PreAuthorizationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*

AbstractAuthenticationProcessingFilter 추상 클래스
웹 기반 인증 요청에 사용. 폼 POST, SSO 정보 또는 기타 사용자가 제공한 크리덴셜(크리덴셜은 사용자가 본인을 증명하는 수단)을 포함한 요청을 처리.
브라우저 기반 HTTP 기반 인증 요청 에서 사용되는 컴포넌트로 POST 폼 데이터를 포함하는 요청을 처리한다.
인증 실패와 인증 성공 관련 이벤트 관련 핸들러 메서드를 가지고 있다.
사용자 비밀번호를 다른 필터로 전달하기 위해서 Authentication 객체를 생성하고 일부 프로퍼티를 설정한다.
간단하게 설명하자면 인증요청에 해당하는 URL 을 감지하면
최초로 AbstractAuthenticationProcessingFilter 를 구현한 클래스(FormLoginFilter)가 요청을 가로챈 후 Authentication 객체를 생성한다.
AbstractAuthenticationProcessingFilter 클래스의 doFilter 메서드로 인해서 가장 처음 인증 attemptAuthentication 메서드를 실행한다.

 */

public class FormLoginFilter extends AbstractAuthenticationProcessingFilter {

    // 3.
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    private AuthenticationFailureHandler authenticationFailureHandler;

    // 1. defaultFilterProcessesUrl - filterProcessesUrl 의 기본생성자 생성
    // 그리고 성공 실패 핸들러를 담은 생성자 두개 만들어 준다.
    // 생성자 1-1
    protected FormLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    // 생성자 1-2
    public FormLoginFilter(
            String defaultUrl,
            AuthenticationSuccessHandler successHandler,
            AuthenticationFailureHandler failureHandler) {
        super(defaultUrl);
        this.authenticationSuccessHandler = successHandler;
        this.authenticationFailureHandler = failureHandler;
    }

    // 2. AbstractAuthenticationProcessingFilter 클래스의 doFilter 메서드로 인해서
    // 가장 처음 인증 attemptAuthentication 메서드를 실행한다
    // 만약 attemptAuthentication 메서드에서 인증이 성공한다면 doFilter 메서드 에서 (AbstractAuthenticationProcessingFilter 87 번 줄)
    // successfulAuthentication 으로 메서드를 실행시키도록 해준다. (인증 실패도 동일)
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest req,
            HttpServletResponse res
    )
            throws AuthenticationException, IOException, ServletException {

        // 사용자입력 ID and Password 를 req 로 받은 값을 ObjectMapper 객체로 JSON 으로 변환하여 AccountFormDTO 형식으로 저장
        // (결과 AccountFormDTO(username=kim, password=1234) 식으로 변환된다.)
        AccountFormDTO dto = new ObjectMapper().readValue(
                req.getReader(),
                AccountFormDTO.class
        );

        // 사용자입력값이 존재하는지 비교하기 위해서 DTO 를 인증 '전' Token 객체에 넣어 PreAuthorizationToken 을 생성합니다.
        // 위 사용자의 값을 가지고 attemptAuthentication는 인증을 시도한다.
        // 인증 시도는 FormLoginAuthenticationProvider 에서 하게된다.
        PreAuthorizationToken token = new PreAuthorizationToken(dto);

        // PreAuthorizationToken 해당 객체에 맞는 Provider를
        // getAuthenticationManager 해당 메서드가 자동으로 찾아서 연결해 준다.
        // 자동으로 찾아준다고 해도 Provider 에 직접 PreAuthorizationToken 지정해 줘야 찾아간다.
        return super
                .getAuthenticationManager()
                .authenticate(token);
    }

    // 4.
    @Override
    protected void successfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {
        this
                .authenticationSuccessHandler
                .onAuthenticationSuccess(req, res, authResult);
    }

    // 4.
    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res,
            AuthenticationException failed
    ) throws IOException, ServletException {
        this
                .authenticationFailureHandler
                .onAuthenticationFailure(req, res, failed);
    }
}
