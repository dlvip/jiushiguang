package com.old.time.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lzy.okgo.model.HttpParams;
import com.old.time.beans.PhoneApiBean;
import com.old.time.beans.PhoneBean;
import com.old.time.beans.PhoneInfo;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PhoneUtils {

    private static final String TAG = "PhoneUtils";

    private static final String[] photos = new String[]{//
            "http://longbei-pro-media-out.oss-cn-hangzhou.aliyuncs.com/sns/2019-2/1126215504598400309.jpg",//
            "http://longbei-pro-media-out.oss-cn-hangzhou.aliyuncs.com/sns/2019-2/1126215504598400236.jpg",//
            "http://longbei-pro-media-out.oss-cn-hangzhou.aliyuncs.com/sns/2019-2/1126215504598400257.jpg",//
            "http://longbei-pro-media-out.oss-cn-hangzhou.aliyuncs.com/sns/2019-2/1126215504598400288.jpg",//
            "http://longbei-pro-media-out.oss-cn-hangzhou.aliyuncs.com/sns/2019-2/1126215504598400205.jpg",//
            "http://longbei-pro-media-out.oss-cn-hangzhou.aliyuncs.com/sns/2019-2/1126215504598400051.jpg",//
            "http://longbei-pro-media-out.oss-cn-hangzhou.aliyuncs.com/sns/2019-2/1126215504598399900.jpg",//
            "http://longbei-pro-media-out.oss-cn-hangzhou.aliyuncs.com/sns/2019-2/1126215504598400123.jpg",//
            "http://longbei-pro-media-out.oss-cn-hangzhou.aliyuncs.com/sns/2019-2/1126215504598400082.jpg",//
            "http://longbei-pro-media-out.oss-cn-hangzhou.aliyuncs.com/sns/2019-2/1126215504598400164.jpg"};

    public static void getPhoneNumberFromMobile(Context context, OnPhoneBeanResultListener onPhoneBeanResultListener) {
        List<PhoneBean> list = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI//
                , new String[]{"display_name", "sort_key", "contact_id", "data1"}//
                , null, null, null);
        nums.clear();
        while (cursor.moveToNext()) {
            //读取通讯录的姓名
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))//
                    .replace(" ", "").replace("+86", "");
            int Id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            String sortKey = getSortKey(cursor.getString(1));
            if (!TextUtils.isEmpty(number) && number.length() == 11) {
                nums.add(number);
                if (!strings.contains(name)) {
                    PhoneBean phoneBean = new PhoneBean(name, number, sortKey, photos[Integer.parseInt(number.substring(10))], Id);
                    list.add(phoneBean);
                    strings.add(name);

                } else {
                    int position = strings.indexOf(name);
                    PhoneBean phoneBean = list.get(position);
                    phoneBean.setNumber(phoneBean.getNumber() + "," + number);

                }
            }
        }
        cursor.close();
        getPhoneBeanList(list, onPhoneBeanResultListener);
    }

    /**
     * 获取手机用户线上通讯录列表
     */
    private static void getPhoneBeanList(final List<PhoneBean> list, final OnPhoneBeanResultListener onPhoneBeanResultListener) {
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_PHONE_BEAN_LIST, new JsonCallBack<ResultBean<List<PhoneBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<PhoneBean>> mResultBean) {
                if (mResultBean == null || mResultBean.data == null) {
                    onPhoneBeanResultListener.onPhoneBeanList(list);

                } else {
                    List<PhoneBean> phoneBeanList = new ArrayList<>();
                    if (list.size() == 0 || mResultBean.data.size() == 0) {
                        phoneBeanList.addAll(list);
                        phoneBeanList.addAll(mResultBean.data);

                    } else {
                        for (PhoneBean phoneBean : list) {
                            for (PhoneBean mPhoneBean : mResultBean.data) {
                                if (phoneBean.getName().equals(mPhoneBean.getName())) {
                                    if (!phoneBean.getNumber().contains(mPhoneBean.getNumber())//
                                            && !mPhoneBean.getNumber().contains(phoneBean.getNumber())) {
                                        phoneBean.setNumber(phoneBean.getNumber() + "," + mPhoneBean.getNumber());

                                    }
                                } else {
                                    phoneBeanList.add(mPhoneBean);

                                }
                            }
                            phoneBeanList.add(phoneBean);
                        }
                    }

                    // 排序
                    Collections.sort(phoneBeanList, new Comparator<PhoneBean>() {

                        @Override
                        public int compare(PhoneBean lhs, PhoneBean rhs) {
                            if (lhs.getName().equals(rhs.getName())) {

                                return lhs.getSortKey().compareTo(rhs.getSortKey());
                            } else {
                                if ("#".equals(lhs.getSortKey())) {

                                    return 1;
                                } else if ("#".equals(rhs.getSortKey())) {

                                    return -1;
                                }
                                return lhs.getSortKey().compareTo(rhs.getSortKey());
                            }
                        }
                    });
                    onPhoneBeanResultListener.onPhoneBeanList(phoneBeanList);
                }
            }

            @Override
            public void onError(ResultBean<List<PhoneBean>> mResultBean) {
                onPhoneBeanResultListener.onPhoneBeanList(list);

            }
        });
    }

    /**
     * 保存通讯录
     *
     * @param phoneBeanList
     */
    public static void savePhoneBeanLIst(List<PhoneBean> phoneBeanList) {
        if (phoneBeanList == null || phoneBeanList.size() == 0) {

            return;
        }
        HttpParams params = new HttpParams();
        params.put("phoneListJson", new Gson().toJson(phoneBeanList));
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        OkGoUtils.getInstance().postNetForData(params, Constant.SAVE_PHONE_BEAN_LIST, new JsonCallBack<ResultBean>() {
            @Override
            public void onSuccess(ResultBean mResultBean) {

            }

            @Override
            public void onError(ResultBean mResultBean) {

            }
        });
    }

    public interface OnPhoneBeanResultListener {

        void onPhoneBeanList(List<PhoneBean> phoneBeans);

    }

    private static List<String> nums = new ArrayList<>();
    private static List<PhoneInfo> phoneInfos = new ArrayList<>();

    /**
     * 手机号归属地
     *
     * @param numStr
     */
    private static void getPhoneMsg(String numStr) {
        HttpParams params = new HttpParams();
        params.put("phone", numStr);
        params.put("key", Constant.PHONE_KEY);
        params.put("dtype", "json");
        OkGoUtils.getInstance().getNetForData(params, Constant.PHONE_DRESS, new JsonCallBack<PhoneApiBean>() {
            @Override
            public void onSuccess(PhoneApiBean mResultBean) {
                if (mResultBean == null || mResultBean.getResult() == null) {

                    return;
                }
                if (mResultBean.getResult() != null) {
                    PhoneInfo phoneInfo = mResultBean.getResult();
                    phoneInfo.setPhone(nums.get(nums.size() - 1));
                    phoneInfos.add(phoneInfo);
                    DebugLog.d(TAG, new Gson().toJson(phoneInfos));

                }
                if (nums.size() > 0) {
                    nums.remove(nums.size() - 1);
                    getPhoneMsg(nums.get(nums.size() - 1));

                } else {
                    String jsonStr = new Gson().toJson(phoneInfos);
                    DebugLog.d(TAG, jsonStr);

                }
            }

            @Override
            public void onError(PhoneApiBean mResultBean) {
                DebugLog.d(TAG, mResultBean.toString());

            }
        });
    }

    private static String getSortKey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {

            return key;
        } else {

            return "#";
        }
    }

    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Context mContext, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        ActivityUtils.startActivity((Activity) mContext, intent);

    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhoneBySelf(Context mContext, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        ActivityUtils.startActivity((Activity) mContext, intent);

    }
}
