package com.old.time.beans;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by NING on 2018/10/16.
 */

public class GoodsBean implements Serializable {

    private String goodsId;

    private String userId;

    private String picKey;

    private String title;

    private String price;

    private String detailId;

    private String isDispose;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPicKey() {
        return picKey;
    }

    public void setPicKey(String picKey) {
        this.picKey = picKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getIsDispose() {
        return isDispose;
    }

    public void setIsDispose(String isDispose) {
        this.isDispose = isDispose;
    }

    public String getDetailStr() {
        if (!TextUtils.isEmpty(detailId)) {

            return "购买";
        }
        return "待处理";
    }
}
