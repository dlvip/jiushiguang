package com.old.time.beans;

public class PhoneApiBean {


    /**
     * resultcode : 200
     * reason : Return Successd!
     * result : {"province":"浙江","city":"杭州","areacode":"0571","zip":"310000","company":"中国移动","card":""}
     */

    private String resultcode;
    private String reason;
    private PhoneInfo result;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public PhoneInfo getResult() {
        return result;
    }

    public void setResult(PhoneInfo result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "PhoneApiBean{" + "resultcode='" + resultcode + '\'' + ", reason='" + reason + '\'' + ", result=" + result + '}';
    }
}
