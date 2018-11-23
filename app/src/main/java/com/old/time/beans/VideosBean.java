package com.old.time.beans;

import java.io.Serializable;

/**
 * Created by wcl on 2018/11/22.
 */

public class VideosBean implements Serializable {

    private String d_pic;
    private String d_id;
    private String d_name;

    public String getD_pic() {
        return d_pic;
    }

    public void setD_pic(String d_pic) {
        this.d_pic = d_pic;
    }

    public String getD_id() {
        return d_id;
    }

    public void setD_id(String d_id) {
        this.d_id = d_id;
    }

    public String getD_name() {
        return d_name;
    }

    public void setD_name(String d_name) {
        this.d_name = d_name;
    }
}
