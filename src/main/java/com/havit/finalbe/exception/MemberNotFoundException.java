package com.havit.finalbe.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(ErrorMsg errorMsg) {
        super(errorMsg.getMessage());
    }
}
