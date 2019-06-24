package com.old.time.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wcl on 2019/6/22.
 */

public class BookMallEntity implements Serializable {


    private String title;

    private List<TabEntity> tabEntities;

    public List<TabEntity> getTabEntities() {
        return tabEntities;
    }

    public void setTabEntities(List<TabEntity> tabEntities) {
        this.tabEntities = tabEntities;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
