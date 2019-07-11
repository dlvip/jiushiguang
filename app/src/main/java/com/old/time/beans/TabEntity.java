package com.old.time.beans;

import java.io.Serializable;

/**
 * Created by wcl on 2019/6/22.
 */

public class TabEntity implements Serializable{

    private String id;

    private String tabId;

    private String name;

    private String pic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTabId() {
        return tabId;
    }

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
