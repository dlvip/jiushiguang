package com.old.time.beans;

import android.content.Context;
import android.text.TextUtils;

import com.old.time.postcard.PCardDetailActivity;
import com.old.time.postcard.UserCardActivity;
import com.old.time.utils.GsonUtils;

import java.io.Serializable;

public class RQCodeBean implements Serializable {

    public static final String MSG_TAG_PHONE_INFO = "0";

    public static final String MSG_TAG_USER_INFO = "1";

    /**
     * 二维码类型
     *
     * @param id
     * @param msgTag
     * @return
     */
    public static RQCodeBean getInstance(String msgTag, String id) {

        return new RQCodeBean(id, msgTag);
    }

    public RQCodeBean(String id, String msgTag) {
        this.id = id;
        this.msgTag = msgTag;

    }

    /**
     * 二维码资源 id
     */
    private String id;

    /**
     * 类型 0：联系人信息、1：用户信息
     */
    private String msgTag;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return msgTag;
    }

    public void setTag(String tag) {
        this.msgTag = tag;
    }

    /**
     * 扫描完成逻辑
     *
     * @param encodeStr
     */
    public static void mRQCodeNext(Context mContext, String encodeStr) {
        if (TextUtils.isEmpty(encodeStr)) {

            return;
        }
        RQCodeBean mRQCodeBean = GsonUtils.jsonToBean(encodeStr, RQCodeBean.class);
        if (mRQCodeBean == null) {

            return;
        }
        switch (mRQCodeBean.getTag()) {//{"id":"50495675245773","msgTag":"0"}
            case MSG_TAG_PHONE_INFO:
                PCardDetailActivity.startPCardDetailActivity(mContext, mRQCodeBean.getId());

                break;
            case MSG_TAG_USER_INFO:
                UserCardActivity.startUserCardActivity(mContext);

                break;
        }
    }
}
