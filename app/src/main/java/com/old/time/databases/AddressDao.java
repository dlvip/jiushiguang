package com.old.time.databases;

import android.content.Context;
import android.database.Cursor;

import com.old.time.beans.AddressBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作地址数据库数据
 *
 * @author WalterWang
 *         Created at 16/1/6 下午3:37
 */
public class AddressDao extends DBDataHelper {

    public AddressDao(Context context) {
        super(context, "drt.db");
    }

    public List<AddressBean> getAddressesByFId(String fid) {

        List<AddressBean> addressBeans = new ArrayList<>();

        try {
            String sqlStr = "select * from drt_address where father_id=?";
            Cursor cursor = getReadableDatabase().rawQuery(sqlStr, new String[]{fid});

            while (cursor != null && cursor.moveToNext()) {

                AddressBean address = new AddressBean();

                address.id = cursor.getInt(0);
                address.zoneAddressId = cursor.getInt(1);
                address.zoneId = cursor.getInt(2);
                address.name = cursor.getString(3);
                address.fatherId = cursor.getInt(4);

                addressBeans.add(address);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return addressBeans;
    }
}
