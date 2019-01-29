package com.old.time.beans;

import android.text.TextUtils;

public class PhoneInfo {

    private String name;
    private String number;
    private String sortKey;
    private int id;

    //显示头
    private boolean isShow;

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean getIsShow() {

        return isShow;
    }

    public PhoneInfo(String name, String number, String sortKey, int id) {
        this.name = name;
        this.number = number;
        this.sortKey = sortKey;
        this.id = id;

    }

    @Override
    public String toString() {
        return "PhoneInfo{" + "name='" + name + '\'' //
                + ", number='" + number + '\'' //
                + ", sortKey='" + sortKey //
                + '\'' + ", id=" + id + '}';
    }

    public String getNamePic() {
        if (TextUtils.isEmpty(name)) {

            return "";
        }

        return name.substring(0, 1);
    }

    public String getName() {
        if (TextUtils.isEmpty(name)) {

            return number;
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
