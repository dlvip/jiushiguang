package com.old.time.beans;

import com.old.time.utils.TimeUtil;

import java.io.Serializable;

/**
 * Created by NING on 2018/9/26.
 */

public class CommentBean implements Serializable {

    private String id;

    private String commentId;

    private String userId;

    private String topicId;

    private String comment;

    private String praiseCount;

    private String createTime;

    private UserInfoBean userEntity;

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

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(String praiseCount) {
        this.praiseCount = praiseCount;
    }

    public String getCreateTime() {

        return TimeUtil.getDateTimeOld(createTime);
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
