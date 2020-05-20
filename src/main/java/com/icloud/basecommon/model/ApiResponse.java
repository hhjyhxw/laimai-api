package com.icloud.basecommon.model;

import lombok.Data;

@Data
public class ApiResponse {
    private int code;
    private String message;
    private Object data;

    public ApiResponse ok(){
        this.setCode(200);
        this.setMessage("成功");
        this.setData(true);
        return this;
    }

    public ApiResponse ok(Object obj){
        this.setCode(200);
        this.setMessage("成功");
        this.setData(obj);
        return this;
    }

    public ApiResponse error(){
        this.setCode(10005);
        this.setMessage("失败");
        this.setData(false);
        return this;
    }

    public ApiResponse error(String message,Object data){
        this.setCode(10005);
        this.setMessage(message);
        this.setData(data);
        return this;
    }


    public ApiResponse okOrError(Boolean result){
        return result==true?this.ok():this.error();
    }
}
