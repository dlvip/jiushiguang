package com.old.time.beans;

import java.io.Serializable;

/**
 * Created by wcl on 2019/4/14.
 */

public class EpisodeEntity implements Serializable {

    private String id;

    /**
     * 视频信息id
     */
    private String videoId;

    /**
     * 播放地址
     */
    private String url;

    /**
     * 创建时间
     */
    private String createTime;

    private boolean isSelect;

    public boolean getSelect() {

        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
