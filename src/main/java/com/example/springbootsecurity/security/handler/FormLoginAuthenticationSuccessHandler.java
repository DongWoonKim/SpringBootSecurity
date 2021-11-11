package com.example.springbootsecurity.security.handler;

import com.example.springbootsecurity.dto.TokenDTO;
import com.example.springbootsecurity.security.jwt.JwtFactroy;
import com.example.springbootsecurity.security.token.PostAuthorizationToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FormLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtFactroy factroy;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest req,
            HttpServletResponse res,
            Authentication auth) throws IOException, ServletException {

        PostAuthorizationToken token = (PostAuthorizationToken) auth;
        UserDetails userDetails = token.getUserDetails();

        String tokenString = factroy.generateToken(userDetails);
        TokenDTO tokenDTO = new TokenDTO(tokenString, userDetails.getUsername());

        processResponse(res, tokenDTO);
    }

    private void processResponse(HttpServletResponse res, TokenDTO tokenDTO) throws IOException {
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setStatus(HttpStatus.OK.value());
        res.getWriter().write(objectMapper.writeValueAsString(tokenDTO));
    }
}
