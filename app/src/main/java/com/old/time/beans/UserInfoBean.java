package com.old.time.beans;

import java.io.Serializable;

/**
 * Created by NING on 2018/3/31.
 */

public class UserInfoBean implements Serializable {


    /**
     * sign :
     * id : d01bbb7c-9f8e-4669-9bbb-7c9f8e1669f0
     * groupCount : 0
     * groupId :
     * frendCount : 0
     * nickName : Akity
     * watchCount : 30
     * age :
     * genger :
     * logopath : 2f362f4da7a4634da090bb8a568e59d7.jpg
     * fansCount : 0
     * bindingTime : 2018-03-06 14:12:34.0
     * focusflag : false
     */

    private String sign;
    private String id;
    private String groupCount;
    private String groupId;
    private String frendCount;
    private String nickName;
    private String watchCount;
    private String age;
    private String genger;
    private String logopath;
    private String fansCount;
    private String bindingTime;
    private boolean focusflag;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(String groupCount) {
        this.groupCount = groupCount;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getFrendCount() {
        return frendCount;
    }

    public void setFrendCount(String frendCount) {
        this.frendCount = frendCount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(String watchCount) {
        this.watchCount = watchCount;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGenger() {
        return genger;
    }

    public void setGenger(String genger) {
        this.genger = genger;
    }

    public String getLogopath() {
        return logopath;
    }

    public void setLogopath(String logopath) {
        this.logopath = logopath;
    }

    public String getFansCount() {
        return fansCount;
    }

    public void setFansCount(String fansCount) {
        this.fansCount = fansCount;
    }

    public String getBindingTime() {
        return bindingTime;
    }

    public void setBindingTime(String bindingTime) {
        this.bindingTime = bindingTime;
    }

    public boolean isFocusflag() {
        return focusflag;
    }

    public void setFocusflag(boolean focusflag) {
        this.focusflag = focusflag;
    }
}
