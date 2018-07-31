package com.old.time.beans;

import java.io.Serializable;

/**
 * Created by wcl on 2018/7/31.
 */

public class IconBean implements Serializable {

    private String id;

    private String iconUrl;

    private String iconTitle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconTitle() {
        return iconTitle;
    }

    public void setIconTitle(String iconTitle) {
        this.iconTitle = iconTitle;
    }
}
