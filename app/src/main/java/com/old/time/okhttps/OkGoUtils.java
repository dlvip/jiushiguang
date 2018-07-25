package com.old.time.okhttps;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

/**
 * Created by wcl on 2018/7/19.
 */

public class OkGoUtils {

    private static OkGoUtils okGoUtils;

    public static OkGoUtils getInstance() {
        if (okGoUtils != null) {

            return okGoUtils;
        }
        okGoUtils = new OkGoUtils();

        return okGoUtils;
    }

    public <T> void postNetForData(String postUrl,  JsonCallBack<T> jsonCallBack) {
        OkGo.<T>post(postUrl).execute(jsonCallBack);
    }

    public <T> void postNetForData(String postUrl, String cacheKey, JsonCallBack<T> jsonCallBack) {
        OkGo.<T>post(postUrl).cacheKey(cacheKey).execute(jsonCallBack);
    }

    public <T> void postNetForData(HttpParams params, String postUrl, String cacheKey, JsonCallBack<T> jsonCallBack) {
        OkGo.<T>post(postUrl).params(params).cacheKey(cacheKey).execute(jsonCallBack);

    }

    public <T> void postNetForData(HttpParams params, String postUrl, JsonCallBack<T> jsonCallBack) {
        OkGo.<T>post(postUrl).params(params).execute(jsonCallBack);

    }
}
