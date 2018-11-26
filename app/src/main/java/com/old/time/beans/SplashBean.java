package com.old.time.beans;

import java.io.Serializable;

/**
 * Created by diliang on 2017/8/4.
 */

public class SplashBean implements Serializable {
    private static final long serialVersionUID = 7382351359868556980L;//这里需要写死 序列化Id

    public String photos;//大图 url
    public String click_url; // 点击跳转 URl
    public String savePath;//图片的存储地址
    public String splashLocal;

    public SplashBean() {

    }

    @Override
    public String toString() {
        return "Splash{" +
                ", photos='" + photos + '\'' +
                ", click_url='" + click_url + '\'' +
                ", savePath='" + savePath + '\'' +
                '}';
    }
}
