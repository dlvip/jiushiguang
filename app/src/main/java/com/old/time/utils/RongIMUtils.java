package com.old.time.utils;

import android.net.Uri;
import android.text.TextUtils;

import com.lzy.okgo.model.HttpParams;
import com.old.time.MyApplication;
import com.old.time.beans.ResultBean;
import com.old.time.beans.UserInfoBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class RongIMUtils {

    private static final String TAG = "RongIMUtils";

    /**
     * 初始化融云
     */
    public static void initRongIM() {
        String phoneNum = PhoneInfoUtils.instance().getNativePhoneNumber(MyApplication.getInstance());
        if (TextUtils.isEmpty(phoneNum)) {

            return;
        }
        HttpParams params = new HttpParams();
        params.put("userId", phoneNum);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_USER_RONG_TOKEN, new JsonCallBack<ResultBean<UserInfoBean>>() {
            @Override
            public void onSuccess(ResultBean<UserInfoBean> mResultBean) {
                if (mResultBean == null || mResultBean.data == null || TextUtils.isEmpty(mResultBean.data.getToken())) {

                    return;
                }
                UserLocalInfoUtils.instance().setmUserInfoBean(mResultBean.data);
                //融云初始化
                RongIM.init(MyApplication.getInstance());
                RongIM.connect(mResultBean.data.getToken(), new RongIMClient.ConnectCallback() {

                    @Override
                    public void onTokenIncorrect() {
                        DebugLog.d(TAG, "onTokenIncorrect");

                    }

                    @Override
                    public void onSuccess(String s) {
                        DebugLog.d(TAG, "onSuccess-用户名：" + s);

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        DebugLog.d(TAG, "onError-错误信息：" + errorCode);

                    }
                });
            }

            @Override
            public void onError(ResultBean<UserInfoBean> mResultBean) {
                DebugLog.d(TAG, mResultBean.msg);

            }
        });
    }


    /**
     * 融云初始化
     */
    public static void RongIMInit() {
        RongIM.init(MyApplication.getInstance());

    }

    /**
     * 链接融云服务器
     */
    public static void RongIMConnect(String token, RongIMClient.ConnectCallback connectCallback) {
        RongIM.connect(token, connectCallback);

    }

    /**
     * 设置当前用户¬信息，
     */
    public static void setCurrentUser() {
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        UserInfoBean userInfoBean = UserLocalInfoUtils.instance().getmUserInfoBean();
        UserInfo userInfo = new UserInfo(userInfoBean.getUserId(), userInfoBean.getUserName(), Uri.parse(GlideUtils.getPicUrl(userInfoBean.getAvatar())));
        RongIM.getInstance().setCurrentUserInfo(userInfo);

    }
}
