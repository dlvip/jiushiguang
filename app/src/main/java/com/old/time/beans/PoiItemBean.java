package com.old.time.beans;

import java.io.Serializable;

/**
 * Created by wcl on 2018/5/16.
 */

public class PoiItemBean implements Serializable {

    public String title;
    public String cityName;
    public String provinceName;
    public String businessArea;
    public boolean isSelect;

    public String getBusinessArea() {
        return businessArea;
    }

    public String getCityName() {
        return cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getTitle() {
        return title;
    }

    public void setBusinessArea(String businessArea) {
        this.businessArea = businessArea;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

