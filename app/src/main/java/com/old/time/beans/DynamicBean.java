package com.old.time.beans;

import android.text.TextUtils;

import com.old.time.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
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

    private List<PhotoInfoBean> contentImages = new ArrayList<>();

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
        if (TextUtils.isEmpty(images)) {

            return contentImages;
        }
        contentImages.clear();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(images);
            for (int i = 0; i < jsonArray.length(); i++) {
                PhotoInfoBean photoWHBean = new PhotoInfoBean();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                photoWHBean.picKey = jsonObject.getString("picKey");
                photoWHBean.with = jsonObject.getInt("with");
                photoWHBean.height = jsonObject.getInt("height");
                contentImages.add(photoWHBean);

            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return contentImages;
    }

    public void setContentImages(List<PhotoInfoBean> contentImages) {
        this.contentImages = contentImages;
    }
}
