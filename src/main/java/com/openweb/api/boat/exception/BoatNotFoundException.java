package com.openweb.api.boat.exception;

import java.io.Serializable;

public class BoatNotFoundException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public BoatNotFoundException(String message) {
        super(message);
    }
}
