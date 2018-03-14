package com.old.time.utils;

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


}
