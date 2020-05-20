package com.icloud.exceptions;

public class AdminServiceException extends ServiceException {


    public AdminServiceException(String message, int code) {
        super(message,code);
    }

    public AdminServiceException(String message) {
        super(message);
    }
}