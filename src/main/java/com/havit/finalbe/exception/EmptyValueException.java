package com.havit.finalbe.exception;

public class EmptyValueException extends RuntimeException {
    public EmptyValueException(ErrorMsg errorMsg) {
        super(errorMsg.getMessage());
    }
}
