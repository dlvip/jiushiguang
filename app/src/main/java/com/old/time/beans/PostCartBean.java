package com.old.time.beans;

import java.util.List;

public class PostCartBean {

    public static PostCartBean getInstance(String codeKey, List<PhoneBean> phoneBeans) {
        return new PostCartBean(codeKey, phoneBeans);
    }

    private PostCartBean(String codeKey, List<PhoneBean> phoneBeans) {
        this.codeKey = codeKey;
        this.phoneBeans = phoneBeans;

    }

    /**
     * 首字母
     */
    private String codeKey;

    /**
     * 首字母归类列表
     */
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
