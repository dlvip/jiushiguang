package com.old.time.okhttps;


import com.old.time.beans.LoginBean;

import java.util.Map;

import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by GaoSheng on 2016/9/13.
 * 网络请求的接口都在这里
 */

public interface HttpService {

    //登录接口
    @Headers("Cache-Control: public, max-age=3600")
    @POST("{apiUrl}")
    Observable<BaseHttpResult<LoginBean>> login(@Path(value = "apiUrl", encoded = true) String apiUrl, @QueryMap Map<String, Object> stringMap);


}
