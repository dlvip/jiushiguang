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
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * Created by huan on 2017/10/20.
 */
public class StringUtils {

    private static final String TAG = "StringUtils";

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
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 十六进制下数字到字符的映射数组
     */
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 对字符串进行MD5编码
     *
     * @param originString
     * @return
     */
    public static String encodeByMD5(String originString) {
        if (originString != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] results = md.digest(originString.getBytes());
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 十六进制字串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 将一个字节转化成16进制形式的字符串
     *
     * @param b
     * @return
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }


    /**
     * 转化分钟
     *
     * @param duration
     * @return
     */
    public static String getDurationStr(int duration) {
        if (duration == 0) {

            return "00:00";
        }
        String durationStr;
        durationStr = duration / 60 > 9 ? "" + duration / 60 : "0" + duration / 60;
        durationStr += duration % 60 > 9 ? ":" + duration % 60 : ":0" + duration % 60;

        return durationStr;
    }

    private static final String[] letters = new String[]{"u", "kf", "av", "mwg", "h", "xn", "yi", "pjz", "b", "qrc"};
    private static final String[] numbers = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    /**
     * 获取加密手机号
     *
     * @return
     */
    public static String getPhoneMobilePassWord(String mobile) {
        if (TextUtils.isEmpty(mobile)) {

            return "";
        }
        String[] mMobileChars = mobile.split("");
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 1; i < mMobileChars.length; i++) {
            stringBuffer.append(letters[Integer.parseInt(mMobileChars[i])]);

        }
        String newMobile = "Mi" + stringBuffer.toString() + "U";
        return newMobile;
    }

    /**
     * 获取解密后的手机号
     *
     * @param mobilePassWord
     * @return
     */
    public static String getPhoneMobile(String mobilePassWord) {
        mobilePassWord = mobilePassWord.replace("Mi", "").replace("U", "");
        for (int i = 0; i < 10; i++) {
            mobilePassWord = mobilePassWord.replace(letters[i], numbers[i]);

        }
        return mobilePassWord;
    }

    /**
     * 转化时间
     *
     * @param timeStr 2019-03-08 17:34:45
     * @return 19/3/8 17:34
     */
    public static String getCreateTime(String timeStr) {
        if (TextUtils.isEmpty(timeStr) || timeStr.length() != 19) {

            return timeStr;
        }

        return timeStr.replace("-", "/").substring(5, 16);
    }

    public static String subString(String text, int num) {
        String content = "";
        if (text.length() > num) {
            content = text.substring(0, num - 1) + "...";
        } else {
            content = text;
        }

        return content;
    }
}
