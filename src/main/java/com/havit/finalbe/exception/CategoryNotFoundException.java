package com.havit.finalbe.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(ErrorMsg errorMsg) {
        super(errorMsg.getMessage());
    }
}
