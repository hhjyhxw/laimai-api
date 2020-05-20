package com.icloud.basecommon.model;

import lombok.Data;

@Data
public class ResultResponse {
    private int errno;
    private String errmsg;
    private Object data;
    private long timestamp;

    public ResultResponse ok(){
        this.setErrno(200);
        this.setErrmsg("成功");
        this.setData(true);
        return this;
    }

    public ResultResponse ok(Object obj){
        this.setErrno(200);
        this.setErrmsg("成功");
        this.setData(obj);
        return this;
    }

    public ResultResponse error(){
        this.setErrno(509);
        this.setErrmsg("失败");
        this.setData(false);
        return this;
    }


    public ResultResponse okOrError(Boolean result){
        return result==true?this.ok():this.error();
    }
}
