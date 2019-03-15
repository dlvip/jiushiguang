package com.old.time.beans;

import java.io.Serializable;

public class JHBaseBean <T> implements Serializable {

    public JHBaseBean(){

    }

    public JHBaseBean(int error_code, String reason) {
        this.error_code = error_code;
        this.reason = reason;
    }

    /**
     * 请求码
     */
    public int error_code;

    /**
     * 具体内容
     */
    public T result;

    /**
     * 提示信息
     */
    public String reason;

}
