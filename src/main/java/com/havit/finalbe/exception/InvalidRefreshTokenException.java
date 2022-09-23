package com.havit.finalbe.exception;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException(ErrorMsg errorMsg) {
        super(errorMsg.getMessage());
    }
}
