package com.old.time.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by NING on 2018/3/10.
 */

public class PicsManageBean implements MultiItemEntity, Serializable {

    public static final int PICS = 1;
    public static final int TEXT = 0;

    public boolean isSelected;

    private int itemType = 0;
    private String createtime;
    private String pickey;

    public String getPickey() {
        return pickey;
    }

    public void setPickey(String pickey) {
        this.pickey = pickey;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

}
