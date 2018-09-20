package com.old.time.utils;

import android.content.Context;

import com.old.time.aidl.ChapterBean;

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
}
