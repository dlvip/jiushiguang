package com.old.time.exceptions;

/**
 * Created by NING on 2018/8/24.
 */

public class JSGRuntimeException extends IllegalStateException {

    private Integer status;
    private String msg;

    public JSGRuntimeException(int status, String msg) {
        super(msg);
        this.status = status;
        this.msg = msg;

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer code) {
        this.status = status;
    }
}
