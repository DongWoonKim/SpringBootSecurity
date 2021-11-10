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

public class FormLoginFilter extends AbstractAuthenticationProcessingFilter {

    // 3
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    private AuthenticationFailureHandler authenticationFailureHandler;

    // 1
    protected FormLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public FormLoginFilter(
            String defaultUrl,
            AuthenticationSuccessHandler successHandler,
            AuthenticationFailureHandler failureHandler) {
        super(defaultUrl);
        this.authenticationSuccessHandler = successHandler;
        this.authenticationFailureHandler = failureHandler;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest req,
            HttpServletResponse res
    ) throws AuthenticationException, IOException, ServletException {

        // JSON 으로 변환
        AccountFormDTO dto = new ObjectMapper().readValue(
                req.getReader(),
                AccountFormDTO.class
        );

        // 사용자 입력값이 존재하는지 비교
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