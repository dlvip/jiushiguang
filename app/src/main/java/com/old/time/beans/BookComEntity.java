package com.old.time.beans;

/**
 * Created by wcl on 2019/3/27.
 */

public class BookComEntity {

    private String id;

    private String bookId;

    private String userId;

    private String comment;

    private String createTime;

    private String praiseCount;

    private UserInfoBean userEntity;

    private BookEntity bookEntity;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setBookEntity(BookEntity bookEntity) {
        this.bookEntity = bookEntity;
    }

    public BookEntity getBookEntity() {
        return bookEntity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public UserInfoBean getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserInfoBean userEntity) {
        this.userEntity = userEntity;
    }
}
