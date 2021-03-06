package com.old.time.utils;

import com.old.time.BuildConfig;
import com.old.time.beans.UserInfoBean;
import com.old.time.constants.Key;

import java.util.Random;

/**
 * Created by NING on 2018/4/5.
 */

public class UserLocalInfoUtils {

    private static UserLocalInfoUtils mUserLocalInfoUtils;

    /**
     * 全局用户信息实例
     *
     * @return
     */
    public static UserLocalInfoUtils instance() {
        if (mUserLocalInfoUtils == null) {
            mUserLocalInfoUtils = new UserLocalInfoUtils();
            mUserLocalInfoUtils.initLocalUserInfo();

        }
        return mUserLocalInfoUtils;
    }

    private UserInfoBean mUserInfoBean;

    private void initLocalUserInfo() {
        mUserInfoBean = SpUtils.getObject(Key.GET_USER_INFO_BEAN);

    }

    public UserInfoBean getmUserInfoBean() {

        return mUserInfoBean;
    }

    /**
     * 获取用户手机号
     *
     * @return
     */
    public String getMobile() {
        if (mUserInfoBean == null) {

            return "-1";
        }

        return mUserInfoBean.getMobile();
    }

    /**
     * 获取用户心id
     *
     * @return
     */
    public String getUserId() {
        if (mUserInfoBean == null) {

            return "-1";
        }
        return mUserInfoBean.getUserId();
    }

    private Random mRandom = new Random();

    /**
     * 获取用户userId
     *
     * @return
     */
    public String getParamUserId() {
        if (mUserInfoBean == null) {

            return "-1";
        }
        if (!BuildConfig.DEBUG && ("15093073252".equals(getMobile()) || "17600075773".equals(getMobile()))) {

            return String.valueOf("01" + mRandom.nextInt(56));
        } else {

            return getUserId();
        }
    }

    /**
     * 设置用户信息
     *
     * @param mUserInfoBean
     */
    public void setmUserInfoBean(UserInfoBean mUserInfoBean) {
        if (mUserInfoBean != null) {
            SpUtils.setObject(Key.GET_USER_INFO_BEAN, mUserInfoBean);

        }
        this.mUserInfoBean = mUserInfoBean;
    }

    /**
     * 获取用户登陆状态
     *
     * @return
     */
    public boolean isUserLogin() {
        if (mUserInfoBean != null) {

            return true;
        }

        return false;
    }

    /**
     * 退出登陆
     */
    public void setUserLogOut() {
        SpUtils.clear();
        mUserInfoBean = null;

    }

    /**
     * 获取融云token
     *
     * @return
     */
    public String getRongIMToken() {
        if (mUserInfoBean == null) {

            return "-1";
        }

        return mUserInfoBean.getToken();
    }
}
