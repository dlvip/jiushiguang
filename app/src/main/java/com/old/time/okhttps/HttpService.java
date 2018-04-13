package com.old.time.okhttps;

import com.old.time.beans.LoginBean;
import com.old.time.beans.UserInfoBean;

import java.util.List;

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
    Observable<BaseHttpResult<LoginBean>> login(@Path("apiUrl") String apiUrl, @Field("json") String mMapParams);

    //获取用户信息
    @FormUrlEncoded
    @POST("{apiUrl}")
    Observable<BaseHttpResult<UserInfoBean>> getUserInfo(@Path("apiUrl") String apiUrl, @Field("json") String mMapParams);

    //发布圈子动态
    @FormUrlEncoded
    @POST("{apiUrl}")
    Observable<BaseHttpResult<UserInfoBean>> sendContent(@Path("apiUrl") String apiUrl, @Field("json") String mMapParams);
    
    //获取内容列表
    @FormUrlEncoded
    @POST("{apiUrl}")
    Observable<BaseHttpResult<List<LoginBean>>> getListContent(@Path("apiUrl") String apiUrl, @Field("json") String mMapParams);



}
