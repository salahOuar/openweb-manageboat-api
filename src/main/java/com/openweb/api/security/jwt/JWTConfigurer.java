package com.openweb.api.security.jwt;

import com.openweb.api.security.filter.JWTFilter;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;

    private final String secret;

    private final String accessTokenCookieName;

    public JWTConfigurer(TokenProvider tokenProvider, String secret, String accessTokenCookieName) {
        this.tokenProvider = tokenProvider;
        this.secret = secret;
        this.accessTokenCookieName = accessTokenCookieName;
    }

    @Override
    public void configure(HttpSecurity http) {
        JWTFilter customFilter = new JWTFilter(tokenProvider, this.secret, this.accessTokenCookieName);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
