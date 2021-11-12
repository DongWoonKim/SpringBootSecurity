package com.example.springbootsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/*
**** Package 구조도 ****
dto : 각 계층간의 데이터 교환을 위해 사용되는 개체 모음
security.filter : req를 interceptor해서 사용자의 인증을 확인하는 클레스 모음
security.handler : 인증 후 결과를 처리해주는 핸들러 클래스 모음
security.jwt : 인증이 완료되면 JWT Token 을 발행해주는 클래스 모음
security.provider :실제 인증을 하는 클래스 UserDetails 객체를 전달 받은 이후 실제 사용자의 입력정보와 UserDetails 객체를 가지고 인증을 시도한다.
security.token : 인증 전 토큰확인과 인증 후 토큰 확인을 해주는 클래스 모음
PreAuthorizationToken : 인증 전 토큰 확인
PostAuthorizationToken : 인증 후 토큰 확인

 */

@EnableJpaAuditing // JPA Auditing 활성화
@SpringBootApplication
public class SpringbootSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSecurityApplication.class, args);
    }

}
