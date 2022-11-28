package com.openweb.api.security.http;


import com.openweb.api.security.dto.LoginDTO;
import com.openweb.api.security.jwt.TokenProvider;
import com.openweb.api.security.service.UserService;
import com.openweb.api.security.util.CookieUtil;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;
    private final UserService userService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final CookieUtil cookieUtil;

    public UserJWTController(TokenProvider tokenProvider, UserService userService, AuthenticationManagerBuilder authenticationManagerBuilder, CookieUtil cookieUtil) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.cookieUtil = cookieUtil;
    }

    @Operation(summary = "Authenticate user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "The user was successfully authenticated", content = {@Content(mediaType = "application/json")}), @ApiResponse(responseCode = "400", description = "Invalid login data", content = @Content)})
    @PostMapping("/authenticate")
    @RateLimiter(name = "rateLimiterApi")
    public ResponseEntity authorize(@Valid @RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);
        HttpHeaders httpHeaders = new HttpHeaders();
        addAccessTokenCookie(httpHeaders, jwt);
        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }

    private void addAccessTokenCookie(HttpHeaders httpHeaders, String idToken) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(idToken, Long.valueOf(20000)).toString());
    }

}
