package com.openweb.api.security.http;


import com.openweb.api.security.dto.UserDTO;
import com.openweb.api.security.exception.AccountResourceException;
import com.openweb.api.security.repository.UserRepository;
import com.openweb.api.security.service.UserService;
import com.openweb.api.security.util.CookieUtil;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class AccountResource {


    private final UserRepository userRepository;
    private final UserService userService;

    private final CookieUtil cookieUtil;


    public AccountResource(UserRepository userRepository, UserService userService, CookieUtil cookieUtil) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.cookieUtil = cookieUtil;
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws AccountResourceException {@code 404 } if the user couldn't be returned.
     */
    @RateLimiter(name = "rateLimiterApi")
    @GetMapping("/account")
    public UserDTO getAccount() {
        try {
            UserDTO userDTO = userService.getUserWithAuthorities().map(user -> new UserDTO(user)).orElseThrow(() -> new AccountResourceException("User could not be found"));
            return userDTO;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    @RateLimiter(name = "rateLimiterApi")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, this.cookieUtil.deleteAccessTokenCookie().toString());
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).build();

    }

}
