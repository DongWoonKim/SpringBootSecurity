package com.example.springbootsecurity.config;

import com.example.springbootsecurity.security.filter.FormLoginFilter;
import com.example.springbootsecurity.security.handler.FormLoginAuthenticationFailureHandler;
import com.example.springbootsecurity.security.handler.FormLoginAuthenticationSuccessHandler;
import com.example.springbootsecurity.security.jwt.HeaderTokenExtractor;
import com.example.springbootsecurity.security.provider.FormLoginAuthenticationProvider;
import com.example.springbootsecurity.security.provider.JWTAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final FormLoginAuthenticationSuccessHandler formLoginAuthenticationSuccessHandler;
    private final FormLoginAuthenticationFailureHandler formLoginAuthenticationFailureHandler;

    private final FormLoginAuthenticationProvider provider;
    private final JWTAuthenticationProvider jwtProvider;
    private final HeaderTokenExtractor headerTokenExtractor;

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    protected FormLoginFilter formLoginFilter() throws Exception {

        FormLoginFilter filter = new FormLoginFilter(
                "/api/account/login",
                formLoginAuthenticationSuccessHandler,
                formLoginAuthenticationFailureHandler
        );

        filter.setAuthenticationManager(super.authenticationManager());

        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(this.provider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable();
        http
                .headers()
                .frameOptions()
                .disable();

        http
                .addFilterBefore(
                        formLoginFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );

    }
}
