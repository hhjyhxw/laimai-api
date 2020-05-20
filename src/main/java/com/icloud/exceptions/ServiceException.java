package com.icloud.exceptions;

import java.io.Serializable;

public abstract class ServiceException extends ApiException implements Serializable {

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

}
