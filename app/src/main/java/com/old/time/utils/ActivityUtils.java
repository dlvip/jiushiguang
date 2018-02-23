package com.old.time.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.old.time.R;

import java.util.HashSet;
import java.util.Set;

/**
 * It use to finish all activities</br> To get start you should：</br> 1： Invoke
 * the method ActivityUtils.add(this) in onCreate method in activties<br>
 * 2： ActivityUtils.remove(this) in onDestroy method in activties.</br>
 * </br> when you quit app,you should invoke finishProgram
 */

@SuppressWarnings("unused")
public class ActivityUtils {
    private static final String TAG = ActivityUtils.class.getSimpleName();

    private static Set<String> activitys = new HashSet<>();
    private static Set<String> activitysLogin = new HashSet<>();// 登陆相关的activity的name


    public static void add(Activity activity) {
        activitys.add(activity.getClass().getSimpleName());
        DebugLog.d(TAG, "add:" + activity.getClass().getSimpleName());
    }

    public static void remove(Activity activity) {
        if (activity != null && activitys.contains(activity.getClass().getSimpleName())) {
            activitys.remove(activity.getClass().getSimpleName());
            DebugLog.d(TAG, "remove:" + activity.getClass().getSimpleName());
        }
    }

    public static void fStartActivtiy(Fragment activity, Intent intent) {
        activity.startActivity(intent);
        activity.getActivity().overridePendingTransition(R.anim.push_in_right, R.anim.push_out_left);

    }

    public static void fStartActivtiyForResult(Fragment activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
        activity.getActivity().overridePendingTransition(R.anim.push_in_right, R.anim.push_out_left);

    }

    public static void startActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_in_right, R.anim.push_out_left);
        add(activity);
    }

    public static void startActivitys(Activity activity, Intent[] intent) {
        activity.startActivities(intent);
        activity.overridePendingTransition(R.anim.push_in_right, R.anim.push_out_left);
        add(activity);
    }


    public static void startNewActivity(Activity activity, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_in_right, R.anim.push_out_left);
    }

    public static void startTopActivity(Context context, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        context.startActivity(intent);
    }

    public static void startPicActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_in_center_tobig, R.anim.push_out_no);
        add(activity);
    }

    public static void finishPicActivity(Activity activity) {
        remove(activity);
        activity.finish();
        activity.overridePendingTransition(R.anim.push_out_no, R.anim.push_in_center_tolitter);
    }

    public static void finishActivity(Activity activity) {
        remove(activity);
        activity.finish();
        activity.overridePendingTransition(R.anim.push_in_left, R.anim.push_out_right);
    }


    public static void startActivityForResult(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.push_in_right, R.anim.push_out_left);
        add(activity);
    }

    /**
     * 向上弹出activity：
     *
     * @param activity
     * @param intent
     */
    public static void startActivityUpAnim(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_in_down, R.anim.push_out_nochange);
        activitys.add(activity.getClass().getSimpleName());
    }

    public static void removeActivity(Activity context) {
        if (activitys.contains(context.getClass().getSimpleName())) {
            activitys.remove(context.getClass().getSimpleName());
        }
    }

    /******************************登录相关****************************/

    /**
     * 向上弹出activity：登陆相关的页面
     *
     * @param activity
     * @param intent
     */
    public static void startLoginActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_in_down, R.anim.push_out_nochange);
        activitysLogin.add(intent.getComponent().getClassName());
    }

    public static void addLoginClass(Intent intent) {
        activitysLogin.add(intent.getComponent().getClassName());

    }

    /**
     * 向下关闭activity ：登陆相关
     *
     * @param activity
     */
    public static void finishLoginActivity(Activity activity) {
        if (activity != null && activitysLogin.contains(activity.getClass().getSimpleName())) {
            activitysLogin.remove(activity);
            DebugLog.d(TAG, "remove:" + activity.getClass().getSimpleName());
        }
        activity.finish();
        activity.overridePendingTransition(R.anim.push_out_nochange, R.anim.push_out_down);
    }

    public static void clearLoginList() {
        activitysLogin.clear();
    }

}
