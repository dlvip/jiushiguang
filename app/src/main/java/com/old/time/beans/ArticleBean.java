package com.old.time.beans;

import java.io.Serializable;

/**
 * Created by wcl on 2018/7/14.
 */

public class ArticleBean implements Serializable {

    public static ArticleBean instance() {
        ArticleBean articleBean = new ArticleBean();
        articleBean.setType("0");
        articleBean.setTitle("我研究了200多个家庭后发现：好的婚姻都做对了这件事！");
        articleBean.setPicUrl("http://5b0988e595225.cdn.sohucs.com/images/20180921/095ebfe7555845b79594f35bef7a7b0d.jpeg");
        articleBean.setDetailUrl("https://www.sohu.com/a/255336680_664958");
        articleBean.setLookCount("189");

        return articleBean;
    }

    private String id;

    private String picUrl;

    private String title;

    private String detailUrl;

    private String type;

    private String lookCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLookCount() {
        return lookCount + "阅读";
    }

    public void setLookCount(String lookCount) {
        this.lookCount = lookCount;
    }
}
