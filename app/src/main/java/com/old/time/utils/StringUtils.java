package com.old.time.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.old.time.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * Created by huan on 2017/10/20.
 */
public class StringUtils {
    public static String getLastPathSegment(String content) {
        if (content == null || content.length() == 0) {
            return "";
        }
        String[] segments = content.split("/");
        if (segments.length > 0) {

            return segments[segments.length - 1];
        }
        return "";
    }

    /**
     * 判断手机格式是否正确
     *
     * @param mobiles 手机号
     */
    public static boolean isMobileNO(String mobiles) {
        if (TextUtils.isEmpty(mobiles)) {

            return false;
        }
        String regExp = "^((13[0-9])|(15[^4,\\D])|(18[0,0-9])|17[0-9]|14[0-9])\\d{8}$";

        Pattern p = Pattern.compile(regExp);

        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    // 验证邮箱地址是否正确
    public static boolean checkEmail(String email) {

        Pattern pattern = Pattern.compile("^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})");
        Matcher mc = pattern.matcher(email);
        return mc.matches();

    }

    /**
     * 获取当前应用程序的版本号
     */
    public static String getVersion(Context context) {
        String st = context.getResources().getString(R.string.Version_number_is_wrong);
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), 0);
            String version = packinfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return st;
        }
    }

    /**
     * 读取本地json
     *
     * @param fileName
     * @param context
     * @return
     */
    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
