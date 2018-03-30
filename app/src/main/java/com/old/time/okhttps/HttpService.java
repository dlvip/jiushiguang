package com.old.time.okhttps;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by GaoSheng on 2016/9/13.
 * 网络请求的接口都在这里
 */

public interface HttpService {

    //登录接口
    @FormUrlEncoded
    @POST("{apiUrl}")
    Observable<BaseHttpResult<Object>> login(@Path("apiUrl") String apiUrl, @Field("json") String mMapParams);


}
