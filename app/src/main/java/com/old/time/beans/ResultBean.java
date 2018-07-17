package com.old.time.beans;

import java.io.Serializable;

/**
 * Created by wcl on 2018/7/17.
 */

public class ResultBean<T> implements Serializable {

    /**请求码*/
    public int status;

    /**具体内容*/
    public T data;

    /**提示信息*/
    public String msg;

}
