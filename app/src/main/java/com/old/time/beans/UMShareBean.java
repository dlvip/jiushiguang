package com.old.time.beans;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by wcliang on 2019/5/24.
 */

public class UMShareBean {

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 图片链接
     */
    private String imgUrl;

    /**
     * 分享链接
     */
    private String shareUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getShareUrl() {

        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
