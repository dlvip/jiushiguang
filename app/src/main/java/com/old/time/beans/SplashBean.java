package com.old.time.beans;

import java.io.Serializable;

/**
 * Created by diliang on 2017/8/4.
 */

public class SplashBean implements Serializable {
    private static final long serialVersionUID = 7382351359868556980L;//这里需要写死 序列化Id
    public int id;
    public int contenttype;//内容类型 0 - 龙榜 1 - 教室  2 - 专题   3 - 达人  4 - 商品
    public int type;//图片类型

    public String photos;//大图 url
    public String click_url; // 点击跳转 URl
    public String savePath;//图片的存储地址
    public String title;//
    public String createtime;//
    public String href;//
    public String isdel;//

    public String splashLocal;

    public SplashBean() {

    }

    @Override
    public String toString() {
        return "Splash{" +
                "id=" + id +
                ", photos='" + photos + '\'' +
                ", type=" + type + '\'' +
                ", href=" + href + '\'' +
                ", contenttype=" + contenttype + '\'' +
                ", click_url='" + click_url + '\'' +
                ", savePath='" + savePath + '\'' +
                '}';
    }
}
