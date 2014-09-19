package com.myapp.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * Created by 庹大伟 on 2014/9/18.
 */
public class NumberAddressQueryUtils {

    //把数据库copy到/data/data/包名/files/database
    private static String path = "data/data/com.myapp.mobilesafe/files/address.db";

    /**
     * 传一个号码，返回一个归属地
     *
     * @param number
     * @return
     */
    public static String queryAddress(String number) {
        String address = number;
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = database.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)", new String[]{TextUtils.substring(number, 0, 7)});
        while (cursor.moveToNext()) {
            String location = cursor.getString(0);
            address = location;
        }
        cursor.close();
        database.close();
        return address;
    }
}
