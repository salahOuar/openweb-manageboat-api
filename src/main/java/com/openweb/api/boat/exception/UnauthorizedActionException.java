package com.openweb.api.boat.exception;

import java.io.Serializable;

public class UnauthorizedActionException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public UnauthorizedActionException() {
        super("Unauthorized action");
    }
}
