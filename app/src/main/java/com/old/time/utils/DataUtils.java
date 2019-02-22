package com.old.time.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lzy.okgo.model.HttpParams;
import com.old.time.aidl.ChapterBean;
import com.old.time.beans.FastMailBean;
import com.old.time.beans.PhoneApiBean;
import com.old.time.beans.PhoneInfo;
import com.old.time.beans.VideosBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/9/20.
 */

public class DataUtils {

    /**
     * 获取数据
     *
     * @param fileName
     * @param mContext
     * @return
     */
    public static List<ChapterBean> getModelBeans(String fileName, Context mContext) {
        String string = StringUtils.getJson(fileName + ".json", mContext);
        List<ChapterBean> chapterBeans = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
            chapterBeans.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject musicObj = jsonArray.getJSONObject(i);
                ChapterBean chapterBean = new ChapterBean();
                chapterBean.setAlbum(musicObj.getString("coverLarge"));
                chapterBean.setAlbumId(Long.parseLong(musicObj.getString("albumId")));
                chapterBean.setAudio(musicObj.getString("playUrl64"));
                chapterBean.setDuration(Long.parseLong(musicObj.getString("duration")));
                chapterBean.setPicUrl(musicObj.getString("coverLarge"));
                chapterBean.setTitle(musicObj.getString("title"));
                chapterBean.setUrl(musicObj.getString("playUrl64"));
                chapterBeans.add(chapterBean);

            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return chapterBeans;
    }

    /**
     * 获取数据
     *
     * @param fileName
     * @param mContext
     * @return
     */
    public static List<VideosBean> getVideosBeans(String fileName, Context mContext) {
        String string = StringUtils.getJson(fileName + ".json", mContext);
        List<VideosBean> chapterBeans = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
            chapterBeans.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject musicObj = jsonArray.getJSONObject(i);
                VideosBean chapterBean = new VideosBean();
                chapterBean.setD_pic(musicObj.getString("d_pic"));
                chapterBean.setD_id(musicObj.getString("d_id"));
                chapterBean.setD_name(musicObj.getString("d_name"));
                chapterBeans.add(chapterBean);

            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return chapterBeans;
    }

    /**
     * 获取手机号归属地
     *
     * @param mContext
     * @return
     */
    public static List<PhoneInfo> getPhoneBeans(Context mContext) {
        String string = StringUtils.getJson("phone.json", mContext);
        List<PhoneInfo> phoneInfos = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(string);
            phoneInfos.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject phoneObj = jsonArray.getJSONObject(i);
                PhoneInfo phoneInfo = new PhoneInfo();
                phoneInfo.setPhone(phoneObj.getString("phone"));
                phoneInfo.setAreacode(phoneObj.getString("areacode"));
                phoneInfo.setCard(phoneObj.getString("card"));
                phoneInfo.setCity(phoneObj.getString("city"));
                phoneInfo.setCompany(phoneObj.getString("company"));
                phoneInfo.setProvince(phoneObj.getString("province"));
                phoneInfo.setZip(phoneObj.getString("zip"));
                phoneInfos.add(phoneInfo);

            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return phoneInfos;
    }

    /**
     * 获取手机号归属地
     *
     * @param phone
     * @return
     */
    public static String getPhoneInfoStr(List<PhoneInfo> phoneInfoList, String phone) {
        String phoneStr = "";
        if (TextUtils.isEmpty(phone) || phoneInfoList == null || phoneInfoList.size() == 0) {

            return phoneStr;
        }
        for (PhoneInfo phoneInfo : phoneInfoList) {
            if (phone.equals(phoneInfo.getPhone())) {
                phoneStr = phoneInfo.getCompany() //
                        + " - " + phoneInfo.getProvince() //
                        + "、" + phoneInfo.getCity();
            }
        }
        if (TextUtils.isEmpty(phoneStr)) {
            getPhoneMsg(phone);

        }
        return phoneStr;
    }

    private static void getPhoneMsg(final String numStr) {
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
                PhoneInfo phoneInfo = mResultBean.getResult();
                phoneInfo.setPhone(numStr);

            }

            @Override
            public void onError(PhoneApiBean mResultBean) {

            }
        });
    }

    /**
     * 获取快递信息
     *
     * @param mContext
     * @return
     */
    public static List<FastMailBean> getFastMailBeans(Context mContext) {
        String string = StringUtils.getJson("fast_mail.json", mContext);
        List<FastMailBean> mFastMailBeans = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(string);
            mFastMailBeans.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject fastMailObj = jsonArray.getJSONObject(i);
                FastMailBean mFastMailBean = new FastMailBean();
                mFastMailBean.setName(fastMailObj.getString("name"));
                mFastMailBean.setIcon(fastMailObj.getString("icon"));
                mFastMailBean.setUrl(fastMailObj.getString("url"));
                mFastMailBean.setId(fastMailObj.getString("id"));
                mFastMailBeans.add(mFastMailBean);

            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return mFastMailBeans;
    }
}
