package com.openweb.api.security.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class CookieUtil {
    @Value("${open.web.jwt.accessTokenCookieName}")
    private String accessTokenCookieName;


    public HttpCookie createAccessTokenCookie(String encryptedToken, Long duration) {
        return ResponseCookie.from(accessTokenCookieName, encryptedToken)
                .maxAge(duration)
                .httpOnly(true)
                .path("/")
                .build();
    }

    public HttpCookie deleteAccessTokenCookie() {
        return ResponseCookie.from(accessTokenCookieName, "").maxAge(0).httpOnly(true).path("/").build();
    }

}