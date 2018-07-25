package com.old.time.okhttps;

import com.lzy.okgo.model.Response;
import com.old.time.beans.ResultBean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by wcl on 2018/7/17.
 */

public abstract class JsonCallBack<T> extends BaseCallback<T> {

    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        Type genType = getClass().getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {

            throw new IllegalStateException("没有填写泛型参数");
        }
        Type type = ((ParameterizedType) genType).getActualTypeArguments()[0];
        JsonConvert<T> convert = new JsonConvert<>(type);

        return convert.convertResponse(response);
    }

    @Override
    public void onSuccess(Response<T> response) {
        if (response == null || response.body() == null) {
            onError((T) new ResultBean(-1, "网络异常"));

        } else {
            onSuccess(response.body());

        }
    }

    @Override
    public void onError(Response<T> response) {
        if (response == null || response.body() == null) {
            onError((T) new ResultBean(-1, "网络异常"));

        } else {
            onError(response.body());

        }
    }

    public abstract void onSuccess(T mResultBean);

    public abstract void onError(T mResultBean);
}
