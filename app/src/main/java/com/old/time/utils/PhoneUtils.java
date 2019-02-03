package com.old.time.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.old.time.beans.PhoneInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PhoneUtils {


    public static List<PhoneInfo> getPhoneNumberFromMobile(Context context) {
        List<PhoneInfo> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI//
                , new String[]{"display_name", "sort_key", "contact_id", "data1"}//
                , null, null, null);
        while (cursor.moveToNext()) {
            //读取通讯录的姓名
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))//
                    .replace(" ", "").replace("+86", "");
            int Id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            String sortKey = getSortkey(cursor.getString(1));
            PhoneInfo phoneInfo = new PhoneInfo(name, number, sortKey, Id);
            list.add(phoneInfo);
            DebugLog.d("phoneInfo:===>", phoneInfo.toString());

        }
        cursor.close();
        // 排序
        Collections.sort(list, new Comparator<PhoneInfo>() {
            @Override
            public int compare(PhoneInfo lhs, PhoneInfo rhs) {
                if (lhs.getName().equals(rhs.getName())) {

                    return lhs.getSortKey().compareTo(rhs.getSortKey());
                } else {
                    if ("#".equals(lhs.getSortKey())) {

                        return 1;
                    } else if ("#".equals(rhs.getSortKey())) {

                        return -1;
                    }
                    return lhs.getSortKey().compareTo(rhs.getSortKey());
                }
            }
        });
        return list;
    }

    private static String getSortkey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {

            return key;
        } else {

            return "#";
        }
    }
}
