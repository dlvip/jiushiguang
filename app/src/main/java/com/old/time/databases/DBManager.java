package com.old.time.databases;


import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class DBManager {
    public static final int DATABASE_VERSION = 1;

    public static final int TYPE_MESSAGE_DB = 2; //个人消息数据库

    private Context context;
    private Map<String, SQLiteOpenHelper> mapHelper;

    public DBManager(Context context) {
        this.context = context;
        mapHelper = new HashMap<>(); // 哈希表用于存数据库
    }

    public SQLiteOpenHelper open(String name, int type) {
        SQLiteOpenHelper helper = mapHelper.get(name);
        if (helper == null) {
            helper = createSQLiteHelper(name, type);
            mapHelper.put(name, helper);
        }

        return helper;
    }

    public void close(String name) {
        SQLiteOpenHelper helper = mapHelper.get(name);
        if (null != helper) {
            mapHelper.remove(helper);
            helper.close();
        }
    }

    public synchronized void clear() {
        Iterator<Entry<String, SQLiteOpenHelper>> iter = mapHelper.entrySet().iterator();
        while (iter.hasNext()) { // 循环迭代器，销毁数据库
            Entry<String, SQLiteOpenHelper> entry = iter.next();
            entry.getValue().close();
        }
        mapHelper.clear();
    }

    private SQLiteOpenHelper createSQLiteHelper(String name, int type) {
        switch (type) {
            case TYPE_MESSAGE_DB:
                return new DBDataHelper(context, name);

            default:
                throw new RuntimeException("this sqlite helper is impossible");
        }
    }

}
