package com.example.springbootsecurity.security.provider;

import com.example.springbootsecurity.security.token.PostAuthorizationToken;
import com.example.springbootsecurity.security.token.PreAuthorizationToken;
import com.example.springbootsecurity.service.account.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class FormLoginAuthenticationProvider implements AuthenticationProvider {

    private final AccountServiceImpl accountService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        PreAuthorizationToken token = (PreAuthorizationToken) authentication;

        String username = token.getName();
        String password = token.getUserPassword();

        UserDetails accountDB = accountService.loadUserByUsername(username);

        if(isCorrectPassword(password, accountDB.getPassword())) {
            return PostAuthorizationToken
                    .getTokenFormUserDetails(accountDB);
        }

        throw new NoSuchElementException("인증 정보가 확인되지 않습니다.");
    }

    private boolean isCorrectPassword(String password, String accountPassword) {
        return passwordEncoder.matches(password, accountPassword);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthorizationToken.class.isAssignableFrom(authentication);
    }
}
