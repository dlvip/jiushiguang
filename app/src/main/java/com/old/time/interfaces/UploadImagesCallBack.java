package com.old.time.interfaces;

import com.old.time.beans.PhotoInfoBean;

import java.util.List;

/**
 * Created by NING on 2018/4/13.
 */

public interface UploadImagesCallBack {

    void getImagesPath(List<PhotoInfoBean> onlineFileName);
}