package com.havit.finalbe.exception;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(ErrorMsg errorMsg) {
        super(errorMsg.getMessage());
    }
}
