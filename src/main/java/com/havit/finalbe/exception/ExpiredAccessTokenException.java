package com.havit.finalbe.exception;

public class ExpiredAccessTokenException extends RuntimeException {
    public ExpiredAccessTokenException(ErrorMsg errorMsg) {
        super(errorMsg.getMessage());
    }
}
