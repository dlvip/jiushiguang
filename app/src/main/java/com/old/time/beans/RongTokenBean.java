package com.old.time.beans;

import java.io.Serializable;

public class RongTokenBean implements Serializable {


    /**
     * code : 200
     * userId : 15093073252
     * token : sign=436efee15fe665957a94d099f22fdddf1adea854x18ywvqfxcbjc15508025300005b9c3&uid=15093073252
     */

    private int code;
    private String userId;
    private String token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRtcToken() {
        return token;
    }

    public void setRtcToken(String token) {
        this.token = token;
    }


}
