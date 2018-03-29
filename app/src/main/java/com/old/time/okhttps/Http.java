package com.old.time.okhttps;


import com.old.time.MyApplication;
import com.old.time.constants.Constant;
import com.old.time.utils.DebugLog;
import com.old.time.utils.NetworkUtil;
import com.old.time.utils.SpUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;


/**
 * Created by GaoSheng on 2016/9/14.
 */

public class Http {

    private static OkHttpClient client;
    private static HttpService httpService;
    private static Retrofit retrofit;


    /**
     * @return retrofit的底层利用反射的方式, 获取所有的api接口的类
     */
    public static HttpService getHttpService() {
        if (httpService == null) {
            httpService = getRetrofit().create(HttpService.class);

        }
        return httpService;
    }

    /**
     * 设置公共参数
     */
    private static Interceptor addQueryParameterInterceptor() {
        Interceptor addQueryParameterInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                if (originalRequest.body() instanceof FormBody) {
                    FormBody body = (FormBody) originalRequest.body();
                    for (int i = 0; i < body.size(); i++) {
                        DebugLog.e("ParamsName=" + body.encodedName(i), ":::ParamsValue=" + body.encodedValue(i));

                    }
                }


                Request request;
                HttpUrl modifiedUrl = originalRequest.url();
                JSONObject jsonObject = new JSONObject();
                try {
                    Set<String> mParametNames = modifiedUrl.queryParameterNames();
                    for (int i = 0; i < mParametNames.size(); i++) {
                        jsonObject.put(modifiedUrl.queryParameterName(i), modifiedUrl.queryParameterValue(i));

                    }
                    for (String name : mParametNames) {
                        modifiedUrl = modifiedUrl.newBuilder().removeAllQueryParameters(name).build();

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
                modifiedUrl = modifiedUrl.newBuilder().setQueryParameter("json", jsonObject.toString()).build();
                request = originalRequest.newBuilder().url(modifiedUrl).build();
                Response mResponse = chain.proceed(request);

                return mResponse;
            }
        };
        return addQueryParameterInterceptor;
    }

    /**
     * 设置头
     */
    private static Interceptor addHeaderInterceptor() {
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder().header("token", (String) SpUtils.get("token", "")).method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        return headerInterceptor;
    }

    /**
     * 设置缓存
     */
    private static Interceptor addCacheInterceptor() {
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetworkUtil.isNetworkAvailable(MyApplication.getInstance())) {
                    request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                }
                Response response = chain.proceed(request);
                if (NetworkUtil.isNetworkAvailable(MyApplication.getInstance())) {
                    int maxAge = 0;
                    // 有网络时 设置缓存超时时间0个小时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
                    response.newBuilder().header("Cache-Control", "public, max-age=" + maxAge).removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    // 无网络时，设置超时为4周  只对get有用,post没有缓冲
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale).removeHeader("nyn").build();
                }
                return response;
            }
        };
        return cacheInterceptor;
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (Http.class) {
                if (retrofit == null) {
                    //添加一个log拦截器,打印所有的log
                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                    //可以设置请求过滤的水平,body,basic,headers
                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    //设置 请求的缓存的大小跟位置
                    File cacheFile = new File(MyApplication.getInstance().getCacheDir(), "cache");
                    Cache cache = new Cache(cacheFile, 1024 * 1024 * 50); //50Mb 缓存的大小

                    client = new OkHttpClient.Builder()
//                            .addInterceptor(addQueryParameterInterceptor())  //参数添加
                            .addInterceptor(addHeaderInterceptor()) // token过滤
                            .addInterceptor(httpLoggingInterceptor) //日志,所有的请求响应度看到
                            .addInterceptor(addCacheInterceptor()).cache(cache)  //添加缓存
                            .connectTimeout(60l, TimeUnit.SECONDS).readTimeout(60l, TimeUnit.SECONDS).writeTimeout(60l, TimeUnit.SECONDS).build();

                    // 获取retrofit的实例
                    retrofit = new Retrofit.Builder().baseUrl(Constant.BASE_URL)  //自己配置
                            .client(client).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()) //这里是用的fastjson的
                            .build();
                }
            }
        }
        return retrofit;
    }
}
