package com.old.time.beans;

import java.io.Serializable;

public class SystemBean implements Serializable {

    /**
     * 版本号
     */
    public int versionCode;

    /**
     * 描述
     */
    public String describe;

    /**
     * 下载URl
     */
    public String url;

    /**
     * 是否强制升级
     */
    public int isForce;

}
