package com.old.time.okhttps;

import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;
import com.lzy.okgo.model.Response;
import com.old.time.utils.DebugLog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by wcl on 2018/7/17.
 */

public class ObjCallBack<T> implements Callback {

    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        String bodyStr = response.body().toString();

        //以下代码是通过泛型解析实际参数,泛型必须传
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];
        if (!(type instanceof ParameterizedType)) throw new IllegalStateException("没有填写泛型参数");

//        JsonReader jsonReader = new JsonReader(response.body().charStream());
//        Type rawType = ((ParameterizedType) type).getRawType();
//        if (rawType == GankResponse.class) {
//            GankResponse gankResponse = Convert.fromJson(jsonReader, type);
//            if (!gankResponse.error) {
//                response.close();
//                //noinspection unchecked
//                return (T) gankResponse;
//            } else {
//                response.close();
//                throw new IllegalStateException("服务端接口错误");
//            }
//        } else {
//            response.close();
//            throw new IllegalStateException("基类错误无法解析!");
//        }
        return null;
    }

    @Override
    public void onStart(Request request) {
        DebugLog.e("request","onStart");

    }

    @Override
    public void onSuccess(Response response) {
        //以下代码是通过泛型解析实际参数,泛型必须传
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];
        if (!(type instanceof ParameterizedType)) throw new IllegalStateException("没有填写泛型参数");
    }

    @Override
    public void onCacheSuccess(Response response) {
        //以下代码是通过泛型解析实际参数,泛型必须传
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];
        if (!(type instanceof ParameterizedType)) throw new IllegalStateException("没有填写泛型参数");
    }

    @Override
    public void onError(Response response) {
        //以下代码是通过泛型解析实际参数,泛型必须传
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];
        if (!(type instanceof ParameterizedType)) throw new IllegalStateException("没有填写泛型参数");
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void uploadProgress(Progress progress) {

    }

    @Override
    public void downloadProgress(Progress progress) {

    }
}
