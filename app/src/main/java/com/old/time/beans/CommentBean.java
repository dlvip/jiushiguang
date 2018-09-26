package com.old.time.beans;

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

    private UserInfoBean userInfoBean;

}
