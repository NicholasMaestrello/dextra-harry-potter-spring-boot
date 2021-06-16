package com.example.dextra.potter.exception;

public class InvalidOperationException extends RuntimeException {

    private int errorCode;
    private String errorMessage;

    public InvalidOperationException(Throwable throwable) {
        super(throwable);
    }

    public InvalidOperationException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public InvalidOperationException(String msg) {
        super(msg);
    }

    public InvalidOperationException(String message, int errorCode) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = message;
    }


    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}