package com.havit.finalbe.exception;

public class InvalidUsernameException extends RuntimeException {
    public InvalidUsernameException(ErrorMsg errorMsg) {
        super(errorMsg.getMessage());
    }
}
