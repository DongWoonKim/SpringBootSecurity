package com.example.springbootsecurity.security.provider;

import com.example.springbootsecurity.dto.AccountDTO;
import com.example.springbootsecurity.security.jwt.JwtDecoder;
import com.example.springbootsecurity.security.token.JwtPreProcessingToken;
import com.example.springbootsecurity.security.token.PostAuthorizationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationProvider implements AuthenticationProvider {

    private final JwtDecoder jwtDecoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String     token      = (String) authentication.getPrincipal();
        AccountDTO accountDTO = jwtDecoder.decodeJwt(token);

        return PostAuthorizationToken.getTokenFormUserDetails(accountDTO);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtPreProcessingToken.class.isAssignableFrom(authentication);
    }
}
