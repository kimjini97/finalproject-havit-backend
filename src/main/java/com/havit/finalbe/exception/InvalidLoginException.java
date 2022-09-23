package com.havit.finalbe.exception;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(ErrorMsg errorMsg) {
        super(errorMsg.getMessage());
    }
}
