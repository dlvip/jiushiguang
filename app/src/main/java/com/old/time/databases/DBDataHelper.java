package com.old.time.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBDataHelper extends SQLiteOpenHelper {

    public DBDataHelper(Context context, String name) {
        super(context, name, null, DBManager.DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        AddressValue value = new AddressValue();
        db.execSQL(value.CREATE_ADDRESS_TABLE);
        db.execSQL(value.INIT_ADDRESS_SQL1);
        db.execSQL(value.INIT_ADDRESS_SQL2);
        db.execSQL(value.INIT_ADDRESS_SQL3);
        db.execSQL(value.INIT_ADDRESS_SQL4);
        db.execSQL(value.INIT_ADDRESS_SQL5);
        db.execSQL(value.INIT_ADDRESS_SQL6);
        db.execSQL(value.INIT_ADDRESS_SQL7);
        db.execSQL(value.INIT_ADDRESS_SQL8);
        db.execSQL(value.INIT_ADDRESS_SQL9);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}