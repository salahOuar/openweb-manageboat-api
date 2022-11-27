package com.openweb.api.security.exception;

import java.io.Serializable;

/**
 *
 */
public class AccountResourceException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public AccountResourceException(String message) {
        super(message);
    }
}
