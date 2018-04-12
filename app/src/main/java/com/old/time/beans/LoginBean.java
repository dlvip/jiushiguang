package com.old.time.beans;

import java.io.Serializable;

/**
 * Created by gaosheng on 2016/12/1.
 * 22:52
 * com.example.gs.mvpdemo.bean
 */

public class LoginBean implements Serializable{

//            "userid": "d01bbb7c-9f8e-4669-9bbb-7c9f8e1669f0",
//            "logopath": "2f362f4da7a4634da090bb8a568e59d7.jpg",
//            "nickname": "Akity"

    /**
     * userId : admin
     * token : success
     */

    private String logopath;
    private String nickname;
    private String userid;

    private String token;

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setLogopath(String logopath) {
        this.logopath = logopath;
    }

    public String getLogopath() {
        return logopath;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUserId() {
        return userid;
    }

    public void setUserId(String userId) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "{" +
                "userId='" + userid + '\'' +
                "nickname='" + nickname + '\'' +
                "logopath='" + logopath + '\'' +
                ", token='" + token + '\'' + "}";
    }
}
