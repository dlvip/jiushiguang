package com.old.time.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LI on 2016/3/17.
 */
public class TimeUtil {
    /**
     * 掉此方法输入所要转换的时间输入例如（"2014年06月14日16时09分00秒"）返回时间戳
     *
     * @param time
     * @return
     */
    public String data(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒", Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 掉此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
     *
     * @param time
     * @return
     */
    public static long dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Long.parseLong(times);
    }

    /**
     * 掉此方法输入所要转换的时间输入例如（"2014-06-14 16:09:00"）返回时间戳
     *
     * @param time
     * @return
     */
    public static long dataToLong(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date;
        long l = 0;
        try {
            date = sdr.parse(time);
            l = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l;
    }

    public static String getTimestamp(String time, String type) {
        SimpleDateFormat sdr = new SimpleDateFormat(type, Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
            Log.d("--444444---", times);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 获取几天前
     *
     * @return
     */
    public static String getTimesBetion(long endTime) {
        long between = (System.currentTimeMillis() - endTime) / 1000;
        long day1 = between / (24 * 3600);
        long hour1 = between % (24 * 3600) / 3600;
        long minute1 = between % 3600 / 60;
        long second1 = between % 60 / 60;
        System.out.println("" + day1 + "天" + hour1 + "小时" + minute1 + "分" + second1 + "秒");
        StringBuffer timeStr = new StringBuffer();
        if (day1 > 0) {
            timeStr.append(day1 + "天前");
        } else if (hour1 > 0) {
            timeStr.append(hour1 + "小时前");
        } else if (minute1 > 0) {
            timeStr.append(minute1 + " 分钟前");
        } else {
            timeStr.append("刚刚");
        }
        return timeStr.toString();
    }

    /**
     * 时间转换 分秒
     *
     * @param times
     * @return
     */
    public static String getTimeStr(int times) {
        int mins = times / 60;
        String minStr, secondStr;
        if (mins > 9) {
            minStr = mins + ":";

        } else {
            minStr = "0" + mins + ":";

        }
        if (times % 60 > 9) {
            secondStr = times % 60 + "";

        } else {
            secondStr = "0" + times % 60;

        }

        return minStr + secondStr;
    }

    /**
     * 获取0点的时间戳
     *
     * @return
     */
    public static long get0dianTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分00秒"）
     *
     * @param time
     * @return
     */
    public String times(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        @SuppressWarnings("unused") long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"06月14日"）
     *
     * @param time
     * @return
     */
    public static String timeh(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("MM月dd日");
        String times = sdr.format(time);
        return times;

    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"NN/06/14"）
     *
     * @param time
     * @return
     */
    public static String longToNYRstr(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");

        String times = sdr.format(new Date(time));
        return times;
    }

    public static String longToNYRhz(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日");
        String times = sdr.format(new Date(time));
        return times;
    }

    public static String dataToNYR(Date time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        String times = sdr.format(time);
        return times;
    }

    public static String dataToYRSF(String data) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(data);
            SimpleDateFormat sdr = new SimpleDateFormat("MM-dd HH:mm");
            String times = sdr.format(date);
            return times;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String longToNYR(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        String times = sdr.format(time);
        return times;
    }

    public static Date NYRSFMTodata(String yymmdd) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(yymmdd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String NYRSFMToNYR(String yymmdd) {
        if (yymmdd.length() > 10) {
            yymmdd = yymmdd.substring(0, 10);
        }
        return yymmdd;
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"yyyy-mm-dd 16:09"）
     *
     * @param time
     * @return
     */
    public static String longToNYRSFstr(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String times = sdr.format(time);
        return times;

    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"06-14 16:09"）
     *
     * @param time
     * @return
     */
    public static String longToYRFMstr(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("MM-dd HH:mm");
        String times = sdr.format(time);
        return times;

    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"16:09"）
     *
     * @param time
     * @return
     */
    public static String longToH_M(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("HH:mm");
        String times = sdr.format(time);
        return times;
    }

    // 调用此方法输入所要转换的时间戳例如（1402733340）输出（"2014/06/14 16:09"）
    public static String times(long timeStamp) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd/  #  HH:mm");
        return sdr.format(new Date(timeStamp)).replaceAll("#", getWeek(timeStamp));

    }

    // 调用此方法输入所要转换的时间戳例如（1402733340）输出（"2014/06/14 16:09"）
    public static String timeIos(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lcc = Long.valueOf(time);
//        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(lcc));

        return times;

    }

    public static String timeStampFormat(long timeStamp) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String times = sdr.format(new Date(timeStamp));
        return times;

    }

    private static String getWeek(long timeStamp) {
        int mydate = 0;
        String week = null;
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(timeStamp));
        mydate = cd.get(Calendar.DAY_OF_WEEK);
        // 获取指定日期转换成星期几
        if (mydate == 1) {
            week = "周日";
        } else if (mydate == 2) {
            week = "周一";
        } else if (mydate == 3) {
            week = "周二";
        } else if (mydate == 4) {
            week = "周三";
        } else if (mydate == 5) {
            week = "周四";
        } else if (mydate == 6) {
            week = "周五";
        } else if (mydate == 7) {
            week = "周六";
        }
        return week;
    }

    /**
     * 获取月份
     *
     * @param timeStamp
     * @return
     */
    public static String getMouth(String timeStamp) {
        String mouthStr = "";
        // 获取指定日期转换成星期几
        if ("01".equals(timeStamp)) {

            mouthStr = "一月";
        } else if ("02".equals(timeStamp)) {

            mouthStr = "二月";
        } else if ("03".equals(timeStamp)) {

            mouthStr = "三月";
        } else if ("04".equals(timeStamp)) {

            mouthStr = "四月";
        } else if ("05".equals(timeStamp)) {

            mouthStr = "五月";
        } else if ("06".equals(timeStamp)) {

            mouthStr = "六月";
        } else if ("07".equals(timeStamp)) {

            mouthStr = "七月";
        } else if ("08".equals(timeStamp)) {

            mouthStr = "八月";
        } else if ("09".equals(timeStamp)) {

            mouthStr = "九月";
        } else if ("10".equals(timeStamp)) {

            mouthStr = "十月";
        } else if ("11".equals(timeStamp)) {

            mouthStr = "十一月";
        } else if ("12".equals(timeStamp)) {

            mouthStr = "十二月";
        }
        return mouthStr;
    }

    // 并用分割符把时间分成时间数组

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14-16-09-00"）
     *
     * @param time
     * @return
     */
    public static String timesOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        long i = Long.parseLong(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    /**
     * 并用分割符把时间分成时间数组
     *
     * @param time
     * @return
     */
    public static String[] timestamp(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        @SuppressWarnings("unused") long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        String[] fenge = times.split("[年月日时分秒]");
        return fenge;
    }

    /**
     * 根据传递的类型格式化时间
     *
     * @param str
     * @param type 例如：yy-MM-dd
     * @return
     */
    public static String getDateTimeByMillisecond(String str, String type) {

        Date date = new Date(Long.valueOf(str));

        SimpleDateFormat format = new SimpleDateFormat(type);

        String time = format.format(date);

        return time;
    }

    /**
     * 根据传递的类型格式化时间
     *
     * @param str
     * @return
     */
    public static String getDateTime(long str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(str);
        String time = format.format(date);

        return time;
    }

    /**
     * 分割符把时间分成时间数组
     *
     * @param time
     * @return
     */
    public String[] division(String time) {

        String[] fenge = time.split("[年月日时分秒]");

        return fenge;

    }

    /**
     * 输入时间戳变星期
     *
     * @param time
     * @return
     */
    public static String changeweek(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        Date date = null;
        int mydate = 0;
        String week = null;
        try {
            date = sdr.parse(times);
            Calendar cd = Calendar.getInstance();
            cd.setTime(date);
            mydate = cd.get(Calendar.DAY_OF_WEEK);
            // 获取指定日期转换成星期几
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mydate == 1) {
            week = "星期日";
        } else if (mydate == 2) {
            week = "星期一";
        } else if (mydate == 3) {
            week = "星期二";
        } else if (mydate == 4) {
            week = "星期三";
        } else if (mydate == 5) {
            week = "星期四";
        } else if (mydate == 6) {
            week = "星期五";
        } else if (mydate == 7) {
            week = "星期六";
        }
        return week;

    }

    /**
     * 获取日期和星期　例如：２０１４－１１－１３　１１:００　星期一
     *
     * @param time
     * @param type
     * @return
     */
    public static String getDateAndWeek(String time, String type) {
        return getDateTimeByMillisecond(time + "000", type) + "  " + changeweekOne(time);
    }

    /**
     * 输入时间戳变星期
     *
     * @param time
     * @return
     */
    public static String changeweekOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        Date date = null;
        int mydate = 0;
        String week = null;
        try {
            date = sdr.parse(times);
            Calendar cd = Calendar.getInstance();
            cd.setTime(date);
            mydate = cd.get(Calendar.DAY_OF_WEEK);
            // 获取指定日期转换成星期几
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mydate == 1) {
            week = "星期日";
        } else if (mydate == 2) {
            week = "星期一";
        } else if (mydate == 3) {
            week = "星期二";
        } else if (mydate == 4) {
            week = "星期三";
        } else if (mydate == 5) {
            week = "星期四";
        } else if (mydate == 6) {
            week = "星期五";
        } else if (mydate == 7) {
            week = "星期六";
        }
        return week;

    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分");
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间 2016-05-06
     *
     * @return
     */
    public static String getCurrentTime2() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 获得当前年
     *
     * @return
     */
    public static int getNowYear() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);    //获取年
        return year;
    }

    /**
     * 获得当前月
     *
     * @return
     */
    public static int getNowMonth() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;//获取月
        return month;
    }

    /**
     * 获得当前日
     *
     * @return
     */
    public static int getNowDay() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH) - 1;//获取日
        return day;
    }

    /**
     * 将时间转成毫秒
     *
     * @param timeStr
     * @return
     */
    public static long getlongTime(String timeStr) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日");
        // 此处会抛异常
        Date date;
        long longDate = 0;
        try {
            date = sdr.parse(timeStr);
            longDate = date.getTime();// 获取毫秒数

        } catch (ParseException e) {
            e.printStackTrace();

        }

        return longDate;
    }

    /**
     * 将时间转成毫秒
     *
     * @param timeStr 格式 yyyy.MM.dd HH:mm
     * @return
     */
    public static long yymmddhmTolong(String timeStr) {
        timeStr = timeStr.replace("-", ".");
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        // 此处会抛异常
        Date date;
        long longDate = 0;
        try {
            date = sdr.parse(timeStr);
            longDate = date.getTime();// 获取毫秒数

        } catch (ParseException e) {
            e.printStackTrace();

        }

        return longDate;
    }

    /**
     * 将时间转成毫秒
     *
     * @param timeStr 格式 yyyy年MM月dd日
     * @return
     */
    public static long yymmddTolong(String timeStr) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日");
        // 此处会抛异常
        Date date;
        long longDate = 0;
        try {
            date = sdr.parse(timeStr);
            longDate = date.getTime();// 获取毫秒数

        } catch (ParseException e) {
            e.printStackTrace();

        }

        return longDate;
    }

    /**
     * 将时间转成毫秒
     *
     * @param timeStr
     * @return
     */
    public static long getTimeLong(String timeStr) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        Date date;
        long longDate = 0;
        try {// 此处会抛异常
            date = sdr.parse(timeStr);
            longDate = date.getTime();// 获取毫秒数

        } catch (ParseException e) {
            e.printStackTrace();

        }

        return longDate;
    }

    /**
     * 输入日期如（2014年06月14日16时09分00秒）返回（星期数）
     *
     * @param time
     * @return
     */
    public String week(String time) {
        Date date = null;
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        int mydate = 0;
        String week = null;
        try {
            date = sdr.parse(time);
            Calendar cd = Calendar.getInstance();
            cd.setTime(date);
            mydate = cd.get(Calendar.DAY_OF_WEEK);
            // 获取指定日期转换成星期几
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mydate == 1) {
            week = "星期日";
        } else if (mydate == 2) {
            week = "星期一";
        } else if (mydate == 3) {
            week = "星期二";
        } else if (mydate == 4) {
            week = "星期三";
        } else if (mydate == 5) {
            week = "星期四";
        } else if (mydate == 6) {
            week = "星期五";
        } else if (mydate == 7) {
            week = "星期六";
        }
        return week;
    }

    /**
     * 输入日期如（2014-06-14-16-09-00）返回（星期数）
     *
     * @param time
     * @return
     */
    public String weekOne(String time) {
        Date date = null;
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        int mydate = 0;
        String week = null;
        try {
            date = sdr.parse(time);
            Calendar cd = Calendar.getInstance();
            cd.setTime(date);
            mydate = cd.get(Calendar.DAY_OF_WEEK);
            // 获取指定日期转换成星期几
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mydate == 1) {
            week = "星期日";
        } else if (mydate == 2) {
            week = "星期一";
        } else if (mydate == 3) {
            week = "星期二";
        } else if (mydate == 4) {
            week = "星期三";
        } else if (mydate == 5) {
            week = "星期四";
        } else if (mydate == 6) {
            week = "星期五";
        } else if (mydate == 7) {
            week = "星期六";
        }
        return week;
    }

    /**
     * @param timeStr 2016-11-17 14:15:42
     * @return
     */
    public static String getDateTimeOld(String timeStr) {
        if (TextUtils.isEmpty(timeStr)) {

            return "";
        }
        long payTime = dataOne(timeStr.replace(" ", "-").replace(":", "-")); //精确到秒的时间
        long nowTime = System.currentTimeMillis() / 1000;//获取手机当前时间 单位 秒
        long between = nowTime - payTime;//剩余时间 单位秒
        long day1 = between / (24 * 60 * 60);
        long hour1 = (between / (60 * 60) - day1 * 24);
        long minute = (between / 60) % 60 == 0 ? 1 : (between / 60) % 60;
        String duiHuanStr = "";

        if (day1 > 3) {
            duiHuanStr = (timeStr).substring(0, 10).replace("-", "/");
        } else if (day1 < 1) {
            if (between - get0dianTime() > 0) {
                duiHuanStr = "昨天";
            } else if (hour1 > 1) {
                duiHuanStr = hour1 + "小时前";
            } else if (minute > 1) {
                duiHuanStr = minute + "分钟前";
            } else {
                duiHuanStr = "刚刚";
            }
        } else {
            duiHuanStr = day1 + "天前";
        }
        return duiHuanStr;
    }

    /**
     * @param timeStamp 1493171138000
     * @return yyyy年MM月dd日 HH:mm
     */
    public static String getTimeStampToString(String timeStamp) {
        long payTime = Long.parseLong(timeStamp) / 1000;
        long nowTime = System.currentTimeMillis() / 1000;//获取手机当前时间 单位 秒
        long between = nowTime - payTime;//剩余时间 单位秒
        long day1 = between / (24 * 60 * 60);
        long hour1 = (between / (60 * 60) - day1 * 24);
        long minute = (between / 60) % 60 == 0 ? 1 : (between / 60) % 60;
        String duiHuanStr = "";
        if (day1 > 3) {
            duiHuanStr = getDateTimeByMillisecond(timeStamp, "yyyy年MM月dd日 HH:mm");

        }
        if (day1 >= 1 && day1 <= 3) {
            if (day1 == 1) {
                duiHuanStr = "昨天";

            } else {
                duiHuanStr = day1 + "天前";

            }
        }
        if (day1 < 1 && hour1 >= 1) {
            duiHuanStr = hour1 + "小时前";

        }

        if (day1 < 1 && hour1 < 1) {
            if (minute >= 1) {
                duiHuanStr = minute + "分钟前";

            } else {
                duiHuanStr = "刚刚";

            }
        }
        return duiHuanStr;
    }

    static String[][] constellations = {{"摩羯座", "水瓶座"}, {"水瓶座", "双鱼座"}, {"双鱼座", "白羊座"}, {"白羊座", "金牛座"}, {"金牛座", "双子座"}, {"双子座", "巨蟹座"}, {"巨蟹座", "狮子座"}, {"狮子座", "处女座"}, {"处女座", "天秤座"}, {"天秤座", "天蝎座"}, {"天蝎座", "射手座"}, {"射手座", "摩羯座"}};
    //星座分割时间
    static int[] date = {20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22};

    //星座生成 传进是日期格式为: yyyy-mm-dd
    public static String getConstellations(String birthday) {
        String[] data = birthday.split("-");
        int day = date[Integer.parseInt(data[1]) - 1];
        String[] cl1 = constellations[Integer.parseInt(data[1]) - 1];
        if (Integer.parseInt(data[2]) >= day) {
            return cl1[1];
        } else {
            return cl1[0];
        }
    }

    public static String getCurrentTimehms() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String millisToString(long millis) {
        millis = millis / 1000;
        long minute = millis / 60;
        long hour = minute / 60;
        long second = millis % 60;
        minute %= 60;
        if (hour > 0) {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        } else {
            return String.format("%02d:%02d", minute, second);
        }
    }

    public static String secondsToString(long seconds) {
        long minute = seconds / 60;
        long hour = minute / 60;
        long second = seconds % 60;
        minute %= 60;
        if (hour > 0) {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        } else {
            return String.format("%02d:%02d", minute, second);
        }
    }


    /**
     * 格式化时间
     *
     * @param timeStamp
     * @param format    格式类型：yyyy-MM-dd HH:mm:ss、.....
     * @return
     */
    public static String getDateTimeFormat(String timeStamp, String format) {
        if (TextUtils.isEmpty(timeStamp)) {

            return "";
        }
        try {
            DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat dformat = new SimpleDateFormat(format);

            return dformat.format(format2.parse(timeStamp));

        } catch (Exception e) {

            DebugLog.d("getDateTimeFormat:", e.getMessage());
        }
        return "";
    }

    /**
     * 带单位的字符串转换为s
     *
     * @param s
     * @return
     */
    public static int strToSecond(String s) {
        int second = 0;
        try {
            if (s.endsWith("秒")) {
                s = s.substring(0, s.length() - 1);
                second = Integer.parseInt(s);
            } else if (s.endsWith("分钟")) {
                s = s.substring(0, s.length() - 2);
                second = Integer.parseInt(s) * 60;
            }
        } catch (Exception e) {
            e.printStackTrace();
            second = 30;
        }
        return second;
    }

    public static String secondToStr(int second) {
        if (second <= 60) {
            return second + "秒";
        }
        if (second > 60) {
            return second / 60 + "分钟";
        }
        return "0";
    }
}
