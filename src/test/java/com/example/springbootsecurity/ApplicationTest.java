package com.example.springbootsecurity;

import com.example.springbootsecurity.constant.UserRole;
import com.example.springbootsecurity.domain.Account;
import com.example.springbootsecurity.repository.AccountRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
// junit 학습 영상 : https://www.youtube.com/watch?v=tyZMdwT3rIY
// https://jojoldu.tistory.com/228
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private AccountRepository accountRepository;

    @After
    public void cleanup() {
        accountRepository.deleteAll();
    }

    @Test
    public void userInsert() {

        // given
        accountRepository.save(
                Account.buildAccount()
                        .username("kim")
                        .password("1234")
                        .role(UserRole.USER)
                        .build()
        );

        // when
        List<Account> userList = accountRepository.findAll();

        // then
        Account account = userList.get(0);
        assertThat(account.getUsername(), is("kim"));
        assertThat(account.getPassword(), is("1234"));
        assertThat(account.getRole(), is(UserRole.USER));
    }

}