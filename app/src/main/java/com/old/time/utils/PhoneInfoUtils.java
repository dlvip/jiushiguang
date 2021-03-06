package com.old.time.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.old.time.MyApplication;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

public class PhoneInfoUtils {

    private static String TAG = "PhoneInfoUtils";

    private static PhoneInfoUtils mPhoneInfoUtils;

    private TelephonyManager telephonyManager;

    public static PhoneInfoUtils instance() {
        if (mPhoneInfoUtils == null) {
            mPhoneInfoUtils = new PhoneInfoUtils();

        }
        return mPhoneInfoUtils;
    }

    private PhoneInfoUtils() {
        if (telephonyManager == null) {
            telephonyManager = (TelephonyManager) MyApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);

        }
    }

    public String getDeviceId(Context context) {
        if (ActivityCompat.checkSelfPermission(context, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return "";
        }
        @SuppressLint("HardwareIds") String mDeviceId = telephonyManager.getDeviceId();

        return mDeviceId;
    }

    //获取sim卡iccid
    public String getIccid(Context context) {
        String iccid = "N/A";
        if (ActivityCompat.checkSelfPermission(context, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
        iccid = telephonyManager.getSimSerialNumber();
        return iccid;
    }

    //获取电话号码
    String getNativePhoneNumber(Context context) {
        String nativePhoneNumber;
        if (ActivityCompat.checkSelfPermission(context, READ_SMS) != PackageManager.PERMISSION_GRANTED //
                && ActivityCompat.checkSelfPermission(context, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return "";
        }
        nativePhoneNumber = telephonyManager.getLine1Number();
        if (TextUtils.isEmpty(nativePhoneNumber) || nativePhoneNumber.length() < 11) {

            return "";
        }
        return nativePhoneNumber.substring(nativePhoneNumber.length() - 11);
    }

    //获取手机服务商信息
    public String getProvidersName() {
        String providersName = "N/A";
        String networkOperator = telephonyManager.getNetworkOperator();
        //IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
//        Flog.d(TAG,"NetworkOperator=" + NetworkOperator);
        if (networkOperator.equals("46000") || networkOperator.equals("46002")) {
            providersName = "中国移动";//中国移动

        } else if (networkOperator.equals("46001")) {
            providersName = "中国联通";//中国联通

        } else if (networkOperator.equals("46003")) {
            providersName = "中国电信";//中国电信

        }
        return providersName;

    }

    public String getPhoneInfo(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        StringBuffer sb = new StringBuffer();
        if (ActivityCompat.checkSelfPermission(context, READ_SMS) != PackageManager.PERMISSION_GRANTED //
                && ActivityCompat.checkSelfPermission(context, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return "";
        }
        sb.append("\nLine1Number = " + tm.getLine1Number());
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());//移动运营商编号
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());//移动运营商名称
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
        sb.append("\nSimOperator = " + tm.getSimOperator());
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
        return sb.toString();
    }
}
