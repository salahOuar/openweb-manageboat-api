package com.openweb.api.boat.handler;


import com.openweb.api.boat.exception.BoatNotFoundException;
import com.openweb.api.boat.exception.UnauthorizedActionException;
import com.openweb.api.security.exception.AccountResourceException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ErrorHandler {


    /**
     * @param e
     * @return
     */
    private static ResponseEntity<String> getStringResponseEntity(RuntimeException e, HttpStatus status) {
        log.error(e.getMessage());
        return ResponseEntity.status(status).body(e.getMessage());
    }

    @ExceptionHandler({RequestNotPermitted.class})
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public void handleRequestNotPermitted() {
    }

    @ExceptionHandler({AccountResourceException.class})
    public ResponseEntity handleResourceNotFoundException(AccountResourceException e) {
        return getStringResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UnauthorizedActionException.class})
    public ResponseEntity handleUnauthorizedActionException(UnauthorizedActionException e) {
        return getStringResponseEntity(e, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler({UsernameNotFoundException.class, AuthenticationException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleAuthenticationException() {
    }

    @ExceptionHandler(BoatNotFoundException.class)
    public ResponseEntity handleException(BoatNotFoundException e) {
        return getStringResponseEntity(e, HttpStatus.NOT_FOUND);
    }
}
