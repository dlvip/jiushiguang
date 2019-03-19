package com.old.time.beans;

import java.io.Serializable;

/**
 * Created by NING on 2018/3/31.
 */

public class UserInfoBean implements Serializable {

    private String id;

    private String userId;

    private String userName;

    private String avatar;

    private String mobile;

    private String birthday;

    /**
     * 0：女、1：男
     */
    private int sex;

    private String vocation;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int isSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getVocation() {
        return vocation;
    }

    public void setVocation(String vocation) {
        this.vocation = vocation;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" + "id='" + id + '\'' + ", userId='" + userId + '\'' + ", userName='" + userName + '\'' + ", avatar='" + avatar + '\'' + ", mobile='" + mobile + '\'' + ", birthday='" + birthday + '\'' + ", sex=" + sex + ", vocation='" + vocation + '\'' + ", token='" + token + '\'' + '}';
    }
}
