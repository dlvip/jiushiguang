package com.old.time.interfaces;

import org.json.JSONObject;

/**
 * Created by LI on 2016/4/22.
 */
public interface AliyPhotoCallbackInterface {
    void requestCode(int code, JSONObject resultData);

}
