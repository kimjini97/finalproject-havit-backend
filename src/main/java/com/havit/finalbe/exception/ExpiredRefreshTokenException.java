package com.havit.finalbe.exception;

public class ExpiredRefreshTokenException extends RuntimeException{

    public ExpiredRefreshTokenException(ErrorMsg errorMsg) {super(errorMsg.getMessage());}
}
