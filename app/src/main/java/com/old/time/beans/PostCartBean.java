package com.old.time.beans;

import java.util.List;

public class PostCartBean {

    public static PostCartBean getInstance(String codeKey, List<PhoneBean> phoneBeans) {
        return new PostCartBean(codeKey, phoneBeans);
    }

    public PostCartBean(String codeKey, List<PhoneBean> phoneBeans) {
        this.codeKey = codeKey;
        this.phoneBeans = phoneBeans;
    }

    private int colorRes;

    private String codeKey;

    private List<PhoneBean> phoneBeans;

    public List<PhoneBean> getPhoneBeans() {
        return phoneBeans;
    }

    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }

    public String getCodeKey() {
        return codeKey;
    }

    public void setPhoneBeans(List<PhoneBean> phoneBeans) {
        this.phoneBeans = phoneBeans;
    }
}
