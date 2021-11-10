package com.example.springbootsecurity.service.account;

import com.example.springbootsecurity.domain.Account;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AccountService extends UserDetailsService {

    @Override
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;

    // 유저의 정보를 DB에 생성 or 변경 의 역할
    Account saveOrUpdateAccount(Account account);
}
