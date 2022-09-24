package com.havit.finalbe.exception;

public class RefreshTokenNotMatched extends RuntimeException{
    public RefreshTokenNotMatched(ErrorMsg errorMsg) {
        super(errorMsg.getMessage());
    }
}
