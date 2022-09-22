package com.havit.finalbe.exception;

public class InvalidAccessTokenException extends RuntimeException {
    public InvalidAccessTokenException(ErrorMsg errorMsg) {
        super(errorMsg.getMessage());
    }
}