package com.old.time.okhttps;

import com.old.time.beans.AlbumBean;
import com.old.time.beans.DynamicBean;
import com.old.time.beans.LoginBean;
import com.old.time.beans.UserInfoBean;
import com.old.time.beans.VideoBean;
import com.old.time.utils.AliyPostUtil;

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
    Observable<BaseHttpResult<String>> sendContent(@Path("apiUrl") String apiUrl, @Field("json") String mMapParams);
    
    //获取内容列表
    @FormUrlEncoded
    @POST("{apiUrl}")
    Observable<BaseHttpResult<List<DynamicBean>>> getListContent(@Path("apiUrl") String apiUrl, @Field("json") String mMapParams);

    //获取视频列表
    @FormUrlEncoded
    @POST("video/{apiUrl}")
    Observable<BaseHttpResult<List<VideoBean>>> getVideoList(@Path("apiUrl") String apiUrl, @Field("json") String mMapParams);

    //获取相册列表
    @FormUrlEncoded
    @POST("album/{apiUrl}")
    Observable<BaseHttpResult<List<AlbumBean>>> getAlbumList(@Path("apiUrl") String apiUrl, @Field("json") String mMapParams);

}
