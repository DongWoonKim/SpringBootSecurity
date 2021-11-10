package com.example.springbootsecurity.domain;

import com.example.springbootsecurity.constant.UserRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Table
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    // http://malevanyy.com/2019/11/how-i-discovered-superbuilder-in-lombok/
    @Builder(builderMethodName = "buildAccount")
    public Account(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role     = role;
    }
}
