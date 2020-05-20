package com.icloud.exceptions;

import lombok.Data;

@Data
public class ApiException extends RuntimeException {

    private int code;
    private String message;
    public ApiException(){
        super();
    }
    public ApiException(String message){
        super(message);
        this.code=1006;
        this.message = message;
    }
    public ApiException(int code,String message){
        super(message);
        this.code=code;
        this.message = message;
    }

}
