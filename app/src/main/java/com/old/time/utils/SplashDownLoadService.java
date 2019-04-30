package com.old.time.utils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.old.time.beans.SplashBean;
import com.old.time.constants.Constant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by diliang on 2017/8/4.
 */

public class SplashDownLoadService extends IntentService {

    private static String TAG = "SplashDownLoadService";

    public SplashDownLoadService() {
        super("SplashDownLoad");
    }

    /**
     * 下载启动页服务
     *
     * @param mContext
     * @param WH
     */
    public static void startDownLoadSplashImage(Context mContext, int[] WH) {
        Intent intent = new Intent(mContext, SplashDownLoadService.class);
        intent.putExtra("WH", WH);
        mContext.startService(intent);
        DebugLog.e(TAG, "图片下载服务开启");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            int[] WH = intent.getIntArrayExtra("WH");
            loadSplashNetDate(WH);

        }
    }

    /**
     * /**
     * 获取图片信息接口
     */
    private void loadSplashNetDate(final int[] WH) {
        SplashBean splashBean = new SplashBean();
        splashBean.photos = "http://puui.qpic.cn/vcover_vt_pic/0/nilk5fd4bkqdk3a1556530742/0";
        splashBean.click_url = Constant.MP4_PATH_URL;
        splashBean.splashLocal = Constant.SPLASH_PATH;
        initDownLoadData(splashBean, WH);
//        NetWorkUtils.getInstance().requestObjData(getApplicationContext(), Constant.GET_SPLISH_BANNER, new XutilParams(), new SplashBean(), new IRequestCallBack() {
//            @Override
//            public void getObject(Object o) {
//                DebugLog.e(TAG, "获取启动页活动信息成功");
//                ResponseObjBean objBean = (ResponseObjBean) o;
//                SplashBean mScreen = (SplashBean) objBean.data;
//                mScreen.splashLocal = Constant.SPLASH_PATH;
//                initDownLoadData(mScreen, WH);
//
//            }
//
//            @Override
//            public void onFailBack(Object o) {
//                ResponseObjBean responseBean = (ResponseObjBean) o;
//                if (getSplashLocal(Constant.SPLASH_PATH) != null && responseBean.code != 404) {
//                    deleteLocalFile(Constant.SPLASH_PATH);
//                    DebugLog.e(TAG, "获取启动页活动信息失败" + responseBean.rtnInfo);
//
//                }
//            }
//        });
    }

    private void initDownLoadData(SplashBean mScreen, int[] WH) {
        SplashBean splashLocal = getSplashLocal(mScreen.splashLocal);
        mScreen.photos = mScreen.photos + "?x-oss-process=image/resize,w_" + WH[0];//宽度填充屏幕，高度自适应
        if (splashLocal == null) {
            DebugLog.e(TAG, "splashLocal 为空导致下载" + mScreen.photos);
            startDownLoadSplash(mScreen, mScreen.splashLocal, mScreen.photos);

        } else if (isNeedDownLoad2(mScreen, splashLocal)) {
            DebugLog.e(TAG, "isNeedDownLoad 导致下载" + mScreen.photos);
            startDownLoadSplash(mScreen, mScreen.splashLocal, mScreen.photos);

        } else {
            DebugLog.e(TAG, "不需要下载");

        }
    }

    /**
     * 删除本地文件
     *
     * @param filePath
     */
    private void deleteLocalFile(String filePath) {
        File splashFile;
        try {
            splashFile = SerializableUtils.getSerializableFile(filePath, Constant.SPLASH_FILE_NAME);
            if (splashFile.exists()) {
                splashFile.delete();
                DebugLog.e(TAG, "mScreen为空删除本地文件");

            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * 获取本地序列化对象
     *
     * @param baseUrl
     * @return
     */
    public static SplashBean getSplashLocal(String baseUrl) {
        SplashBean splash = null;
        try {
            File splashFile = SerializableUtils.getSerializableFile(baseUrl, Constant.SPLASH_FILE_NAME);
            splash = (SplashBean) SerializableUtils.readObject(splashFile);

        } catch (Exception e) {
            DebugLog.e(TAG, "SplashActivity 获取本地序列化闪屏失败" + e.getMessage());

        }
        return splash;
    }

    /**
     * 判断是否需要下载
     *
     * @param mScreenSplash 线上数据
     * @param mLocalSplash  本地数据
     * @return 图片路径发生改变需要下载
     */
    private boolean isNeedDownLoad2(SplashBean mScreenSplash, SplashBean mLocalSplash) {
        if (mLocalSplash == null) {
            DebugLog.e(TAG, "splashLocal 为空导致下载");

            return true;
        }
        if (TextUtils.isEmpty(mLocalSplash.savePath)) {

            return true;
        }
        File file = new File(mLocalSplash.savePath);
        if (!file.exists()) {

            return true;
        }
        if (TextUtils.isEmpty(mLocalSplash.photos) || !mLocalSplash.photos.equals(mScreenSplash.photos)) {
            DebugLog.e(TAG, "图片网络地址改变导致下载");

            return true;
        }
        //更新数据
        mScreenSplash.splashLocal = mLocalSplash.splashLocal;//bean存储地址
        mScreenSplash.savePath = mLocalSplash.savePath;//图片存储地址
        SerializableUtils.writeObject(mScreenSplash, mScreenSplash.splashLocal + "/" + Constant.SPLASH_FILE_NAME);

        return false;
    }

    /**
     * @param lastUrl  本地保存de网络图片url
     * @param lastSave 本地存储的图片绝对路径
     * @param url      网络图片url
     * @return 比较储存的 图片名称的哈希值与 网络获取的哈希值是否相同
     */
    private boolean isNeedDownLoad(String lastUrl, String lastSave, String url) {
        if (TextUtils.isEmpty(lastSave)) {

            return true;
        }
        File file = new File(lastSave);
        if (!file.exists()) {

            return true;
        }
        if (getImageName(lastUrl).hashCode() != getImageName(url).hashCode()) {

            return true;
        }
        return false;
    }


    private String getImageName(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        url = url.substring(0, url.lastIndexOf("?"));
        String[] split = url.split("/");
        String fileName = split[split.length - 1];
        return fileName;
    }

    /**
     * 开始下载
     *
     * @param mScreen    网络数据
     * @param splashPath 本地地址
     * @param burl       线上图片地址
     */
    private void startDownLoadSplash(final SplashBean mScreen, final String splashPath, String burl) {
        DownLoadUtils.downLoad(splashPath, new DownLoadUtils.DownLoadInterFace() {

            @Override
            public void afterDownLoad(ArrayList<String> savePaths) {
                if (savePaths.size() == 1) {
                    DebugLog.e(TAG, "闪屏页面下载完成" + savePaths);
                    if (mScreen != null) {
                        mScreen.savePath = savePaths.get(0);
                        SerializableUtils.writeObject(mScreen, splashPath + "/" + Constant.SPLASH_FILE_NAME);

                    }
                } else {
                    deleteLocalFile(splashPath);
                    DebugLog.e(TAG, "闪屏页面下载失败" + savePaths);

                }
            }
        }, burl);
    }

    @Override
    public void onDestroy() {
        DebugLog.e(TAG, "onDestroy");

        super.onDestroy();
    }
}
