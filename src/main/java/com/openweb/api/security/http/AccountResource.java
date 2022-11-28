package com.openweb.api.security.http;


import com.openweb.api.security.dto.UserDTO;
import com.openweb.api.security.exception.AccountResourceException;
import com.openweb.api.security.repository.UserRepository;
import com.openweb.api.security.service.UserService;
import com.openweb.api.security.util.CookieUtil;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Retrieve current user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "The current user data", content = {@Content(mediaType = "application/json")}), @ApiResponse(responseCode = "404", description = "User could not be found", content = @Content)})
    @RateLimiter(name = "rateLimiterApi")
    @GetMapping("/account")
    public UserDTO getAccount() {
        return userService.getUserWithAuthorities().map(user -> new UserDTO(user)).orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    /**
     * @param request
     * @param response
     * @return
     */
    @Operation(summary = "Log out current user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "The current user has been successfully logged out", content = {@Content(mediaType = "application/json")})})
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
