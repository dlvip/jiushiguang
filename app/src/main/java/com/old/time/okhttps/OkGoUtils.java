package com.old.time.okhttps;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.old.time.beans.ArticleBean;
import com.old.time.beans.ResultBean;

import java.util.List;

/**
 * Created by wcl on 2018/7/19.
 */

public class OkGoUtils {

    public static <T> void postNetForData(String postUrl, String cacheKey, final JsonObjCallBack<T> jsonObjCallBack) {
        OkGo.<ResultBean<List<ArticleBean>>>post(postUrl).cacheKey(cacheKey).execute(new JsonCallBack<ResultBean<List<ArticleBean>>>() {

            @Override
            public void onSuccess(Response<ResultBean<List<ArticleBean>>> response) {
                if (response == null) {
                    jsonObjCallBack.onError(new ResultBean(-1, "网络异常"));

                }
                jsonObjCallBack.onSuccess((ResultBean) response.body());

            }

            @Override
            public void onError(Response<ResultBean<List<ArticleBean>>> response) {
                if (response == null) {
                    jsonObjCallBack.onError(new ResultBean(-1, "网络异常"));

                }
                jsonObjCallBack.onError((ResultBean) response.body());

            }
        });
    }

    public interface JsonObjCallBack<T> {

        void onSuccess(ResultBean<T> mResultBean);

        void onError(ResultBean<T> mResultBean);

    }
}
