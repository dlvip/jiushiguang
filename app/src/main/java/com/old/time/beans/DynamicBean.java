package com.old.time.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wcl on 2018/4/14.
 */

public class DynamicBean implements Serializable {

    private String id;
    private String userId;
    private String images;
    private String content;
    private String createTime;
    private List<PhotoInfoBean> contentImages;

    private UserInfoBean userInfoBean;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<PhotoInfoBean> getContentImages() {
        return contentImages;
    }

    public void setContentImages(List<PhotoInfoBean> contentImages) {
        this.contentImages = contentImages;
    }

    public UserInfoBean getUserInfoBean() {
        return userInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }
}
