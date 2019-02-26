package com.old.time.utils;

import android.util.Base64;

import com.google.gson.Gson;

import java.io.Serializable;

public class Base64Utils {

    /**
     * base64加密
     *
     * @param serializable
     * @return
     */
    public static String encodeToString(Serializable serializable) {
        String obgStr = new Gson().toJson(serializable);

        return Base64.encodeToString(obgStr.getBytes(), Base64.DEFAULT);
    }

    /**
     * base64解密
     *
     * @param encodeStr
     * @return
     */
    public static String decode(String encodeStr) {
         byte[] bytes = Base64.decode(encodeStr.getBytes(), Base64.DEFAULT);

        return new String(bytes);
    }

}
