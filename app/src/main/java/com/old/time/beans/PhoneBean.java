package com.old.time.beans;

import java.util.List;

public class PhoneBean {

    public static PhoneBean getInstance(String codeKey, List<PhoneInfo> phoneInfos) {
        return new PhoneBean(codeKey, phoneInfos);
    }

    public PhoneBean(String codeKey, List<PhoneInfo> phoneInfos) {
        this.codeKey = codeKey;
        this.phoneInfos = phoneInfos;
    }

    private int colorRes;

    private String codeKey;

    private List<PhoneInfo> phoneInfos;

    public List<PhoneInfo> getPhoneInfos() {
        return phoneInfos;
    }

    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }

    public String getCodeKey() {
        return codeKey;
    }

    public void setPhoneInfos(List<PhoneInfo> phoneInfos) {
        this.phoneInfos = phoneInfos;
    }
}
