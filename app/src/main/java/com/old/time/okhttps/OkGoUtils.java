package com.old.time.okhttps;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.old.time.beans.ResultBean;

/**
 * Created by wcl on 2018/7/19.
 */

public class OkGoUtils {

    public static void postNetForData(String postUrl, String cacheKey, final JsonObjCallBack jsonObjCallBack) {
        OkGo.<String>post(postUrl).cacheKey(cacheKey).execute(new JsonCallBack<String>() {

            @Override
            public void onSuccess(Response<String> response) {

                String bodyStr = response.body().toString();
                if (TextUtils.isEmpty(bodyStr)) {
                    ResultBean<String> resultBean = new ResultBean<>();
                    resultBean.status = -1;
                    resultBean.msg = "网络错误";
                    resultBean.data = "";
                    jsonObjCallBack.onSuccess(resultBean);

                    return;
                }
                jsonObjCallBack.onSuccess(new Gson().fromJson(bodyStr, ResultBean.class));

            }

            @Override
            public void onError(Response<String> response) {
                String bodyStr = response.body().toString();
                if (TextUtils.isEmpty(bodyStr)) {
                    ResultBean<String> resultBean = new ResultBean<>();
                    resultBean.status = -1;
                    resultBean.msg = "网络错误";
                    resultBean.data = "";
                    jsonObjCallBack.onSuccess(resultBean);

                    return;
                }
                jsonObjCallBack.onSuccess(new Gson().fromJson(bodyStr, ResultBean.class));

            }
        });
    }

    public interface JsonObjCallBack<T> {

        void onSuccess(ResultBean<T> mResultBean);

        void onError(ResultBean<T> mResultBean);

    }
}
