package com.old.time.beans;

import java.io.Serializable;

/**
 * Created by wcl on 2018/7/30.
 */

public class CourseBean implements Serializable {

    public String id;

    /**
     * 用户di
     */
    public String userId;

    public String title;

    public String coursePic;

    /**
     * 专辑id
     */
    public String albumId;


    @Override
    public String toString() {

        return "CourseBean={ id:" + id + " , userId:" + userId + " , title:" + title + " , coursePic:" + coursePic + " , albumId:" + albumId + "}";
    }
}
