package com.havit.finalbe.exception;

public class NeedRefreshTokenException extends RuntimeException {
    public NeedRefreshTokenException(ErrorMsg errorMsg) {
        super(errorMsg.getMessage());
    }
}
