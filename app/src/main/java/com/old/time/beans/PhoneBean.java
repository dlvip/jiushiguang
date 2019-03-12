package com.old.time.beans;

import android.text.TextUtils;

import java.io.Serializable;

public class PhoneBean implements Serializable {

    private String photo;
    private String name;
    private String number;
    private String sortKey;
    private String id;

    public PhoneBean(String name, String number, String sortKey, String photo) {
        this.name = name;
        this.number = number;
        this.sortKey = sortKey;
        this.photo = photo;

    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "PhoneBean{" + "name='" + name + '\'' //
                + ", number='" + number + '\'' //
                + ", sortKey='" + sortKey //
                + '\'' + ", id=" + id + '}';
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
