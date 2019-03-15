package com.old.time.utils;

import android.content.Context;
import com.lzy.okgo.model.HttpParams;
import com.old.time.aidl.ChapterBean;
import com.old.time.beans.FastMailBean;
import com.old.time.beans.JHBaseBean;
import com.old.time.beans.PhoneInfo;
import com.old.time.beans.ResultBean;
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

    public static void getPhoneMsg(final String numStr) {
        HttpParams params = new HttpParams();
        params.put("phone", numStr);
        params.put("key", Constant.PHONE_KEY);
        params.put("dtype", "json");
        OkGoUtils.getInstance().getNetForData(params, Constant.PHONE_DRESS, new JsonCallBack<JHBaseBean<PhoneInfo>>() {

            @Override
            public void onSuccess(JHBaseBean<PhoneInfo> mResultBean) {
                if (mResultBean == null || mResultBean.result == null) {

                    return;
                }
                PhoneInfo phoneInfo = mResultBean.result;
                phoneInfo.setPhone(numStr);
                savePhoneInfo(phoneInfo);
            }

            @Override
            public void onError(JHBaseBean<PhoneInfo> mResultBean) {

            }
        });
    }

    /**
     * 保存手机归属地
     *
     * @param phoneInfo
     */
    private static void savePhoneInfo(PhoneInfo phoneInfo) {
        HttpParams params = new HttpParams();
        params.put("phone", phoneInfo.getPhone());
        params.put("province", phoneInfo.getProvince());
        params.put("city", phoneInfo.getCity());
        params.put("areacode", phoneInfo.getAreacode());
        params.put("zip", phoneInfo.getZip());
        params.put("company", phoneInfo.getCompany());
        params.put("card", phoneInfo.getCard());
        OkGoUtils.getInstance().postNetForData(params, Constant.SAVE_PHONE_INFO, new JsonCallBack<ResultBean>() {
            @Override
            public void onSuccess(ResultBean mResultBean) {

            }

            @Override
            public void onError(ResultBean mResultBean) {

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

    public static List<String> getDateStrings(int size) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            strings.add("");

        }
        return strings;
    }
}
