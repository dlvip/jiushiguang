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

    public <T> void postNetForData(String postUrl, JsonCallBack<T> jsonCallBack) {
        OkGo.<T>post(postUrl).execute(jsonCallBack);
    }

    public <T> void postNetForData(HttpParams params, String postUrl, JsonCallBack<T> jsonCallBack) {
        String cacheKey = postUrl + (params == null ? "" : params.toString());
        OkGo.<T>post(postUrl).params(params).cacheKey(cacheKey).execute(jsonCallBack);

    }

    public <T> void postNetForData(String cacheParam, HttpParams params, String postUrl, JsonCallBack<T> jsonCallBack) {
        String cacheKey = postUrl + cacheParam;
        OkGo.<T>post(postUrl).params(params).cacheKey(cacheKey).execute(jsonCallBack);

    }

    public <T> void getNetForData(HttpParams params, String postUrl, JsonCallBack<T> jsonCallBack) {
        String cacheKey = postUrl + (params == null ? "" : params.toString());
        OkGo.<T>get(postUrl).params(params).cacheKey(cacheKey).execute(jsonCallBack);

    }
}
