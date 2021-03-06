package com.old.time.beans;

import java.io.Serializable;

public class SignNameEntity implements Serializable {

    private String id;

    private String userId;

    private String picUrl;

    private String content;

    private String shareCount;

    private String paiseCount;

    private String creatTime;

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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShareCount() {
        return shareCount;
    }

    public void setShareCount(String shareCount) {
        this.shareCount = shareCount;
    }

    public String getPaiseCount() {
        return paiseCount;
    }

    public void setPaiseCount(String paiseCount) {
        this.paiseCount = paiseCount;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    private boolean isPaise;

    public boolean getIsPaise() {
        return isPaise;
    }

    public void setIsPaise(boolean isPaise) {
        this.isPaise = isPaise;
    }

    private UserInfoBean userEntity;

    public UserInfoBean getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserInfoBean userEntity) {
        this.userEntity = userEntity;
    }

    private BookEntity bookEntity;

    public BookEntity getBookEntity() {
        return bookEntity;
    }

    public void setBookEntity(BookEntity bookEntity) {
        this.bookEntity = bookEntity;
    }
}
