package com.old.time.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wcl on 2018/4/14.
 */

public class DynamicBean implements Serializable {

    private String id;

    private String userId;

    private String images;

    private String content;

    private String topicId;

    private String createTime;

    private String commentCount;

    private String praiseCount;

    private UserInfoBean userEntity;

    private List<PhotoInfoBean> contentImages;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(String praiseCount) {
        this.praiseCount = praiseCount;
    }

    public UserInfoBean getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserInfoBean userEntity) {
        this.userEntity = userEntity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<PhotoInfoBean> getContentImages() {
        return contentImages;
    }

    public void setContentImages(List<PhotoInfoBean> contentImages) {
        this.contentImages = contentImages;
    }
}
