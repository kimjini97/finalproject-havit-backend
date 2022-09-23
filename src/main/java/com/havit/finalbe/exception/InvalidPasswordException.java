package com.havit.finalbe.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(ErrorMsg errorMsg) {
        super(errorMsg.getMessage());
    }
}
