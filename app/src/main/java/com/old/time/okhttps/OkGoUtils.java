package com.old.time.okhttps;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.old.time.beans.ResultBean;

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

    public <T> void postNetForData(String postUrl, final JsonObjCallBack<T> jsonObjCallBack) {
        OkGo.<T>post(postUrl).execute(new MyJsonCallBack(jsonObjCallBack));
    }

    public <T> void postNetForData(String postUrl, String cacheKey, JsonObjCallBack<T> jsonObjCallBack) {
        OkGo.<T>post(postUrl).cacheKey(cacheKey).execute(new MyJsonCallBack(jsonObjCallBack));

    }

    public <T> void postNetForData(HttpParams params, String postUrl, String cacheKey, JsonObjCallBack<T> jsonObjCallBack) {
        OkGo.<T>post(postUrl).params(params).cacheKey(cacheKey).execute(new MyJsonCallBack(jsonObjCallBack));

    }

    public <T> void postNetForData(HttpParams params, String postUrl, JsonObjCallBack<T> jsonObjCallBack) {
        OkGo.<T>post(postUrl).params(params).execute(new MyJsonCallBack(jsonObjCallBack));

    }

    /**
     * 接口请求返回处理
     *
     * @param <T>
     */
    public class MyJsonCallBack<T> extends JsonCallBack<T> {

        private JsonObjCallBack<T> jsonObjCallBack;

        public MyJsonCallBack(JsonObjCallBack<T> jsonObjCallBack) {
            this.jsonObjCallBack = jsonObjCallBack;

        }

        @Override
        public void onSuccess(Response<T> response) {
            if (response == null) {
                jsonObjCallBack.onError((T) new ResultBean(-1, "网络异常"));

            } else {
                jsonObjCallBack.onSuccess(response.body());

            }
        }

        @Override
        public void onError(Response<T> response) {
            if (response == null) {
                jsonObjCallBack.onError((T) new ResultBean(-1, "网络异常"));

            } else {
                jsonObjCallBack.onError(response.body());

            }
        }
    }

    public interface JsonObjCallBack<T> {
        /**
         * 请求成功
         *
         * @param t
         */
        void onSuccess(T t);

        /**
         * 请求失败
         *
         * @param t
         */
        void onError(T t);

    }
}
