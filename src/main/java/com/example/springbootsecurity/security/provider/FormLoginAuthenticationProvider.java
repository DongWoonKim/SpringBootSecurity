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
    //PasswordEncoder 클래스를 주입 받으려면 PasswordEncoder Bean 을 등록해 줘야합니다.
    private final PasswordEncoder passwordEncoder;

    // 1.
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 2. 기본적으로 이전에 받은 로그인 정보를 담고 있는 Authentication 객체 PreToken 값을 가지고 있다.
        PreAuthorizationToken token = (PreAuthorizationToken) authentication;
        // 해당 PreToken 에서 로그인한 유저의 정보를 변수에 담는다.
        String username = token.getName();
        String password = token.getUserPassword();

        // 3. 로그인한 유저가 DB에 존재하는지 accountService 를 통해서 확인한다.
        UserDetails accountDB = accountService.loadUserByUsername(username);

        // 4. 로그인한 유저와 DB에 존재하는 유저의 Password 가 동일한지 조회하는 메서드이다. *무조건 비교 대상이 앞에 와야한다.
        if(isCorrectPassword(password, accountDB.getPassword())) {
            // 로그인한 유저가 DB에 존재한다면 PostAuthorizationToken(권한이 부여된 토큰) 객체를 생성하여 return 한다.
            return PostAuthorizationToken
                    .getTokenFormUserDetails(accountDB);
        }

        // 이곳까지 통과하지 못했다면 잘못된 요청으로 접근하지 못한 것.
        throw new NoSuchElementException("인증 정보가 확인되지 않습니다.");
    }

    private boolean isCorrectPassword(String password, String accountPassword) {
        return passwordEncoder.matches(password, accountPassword);
    }

    // 5.
    // Provider 를 연결 해주는 메소드 PreAuthorizationToken 사용한 filter 를 검색 후 연결
    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthorizationToken.class.isAssignableFrom(authentication);
    }
}
