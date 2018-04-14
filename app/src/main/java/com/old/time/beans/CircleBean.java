package com.old.time.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wcl on 2018/4/14.
 */

public class CircleBean implements Serializable {
    public String id;
    public String conetentTitle;
    public String conetent;
    public String createTimeStr;
    public String commentCount;
    public List<PhotoInfoBean> conetentImages;


}
