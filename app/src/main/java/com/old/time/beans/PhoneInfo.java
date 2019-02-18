package com.old.time.beans;

import android.text.TextUtils;

import java.io.Serializable;

public class PhoneInfo implements Serializable {

    private String photo;
    private String name;
    private String number;
    private String sortKey;
    private int id;

    public PhoneInfo(String name, String number, String sortKey, String photo, int id) {
        this.name = name;
        this.number = number;
        this.sortKey = sortKey;
        this.photo = photo;
        this.id = id;

    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
