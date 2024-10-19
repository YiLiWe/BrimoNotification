package com.example.brimonotification.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.lang.reflect.Field;

public class MyDBOpenHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "data.db";
    private static final int VERSION = 1;

    public MyDBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table notification" +
                "(" +
                "    id           integer" +
                "        constraint notification_pk" +
                "            primary key autoincrement," +
                "    amount       text," +
                "    payerName    text," +
                "    account      text," +
                "    noticeTime   text," +
                "    state        integer default 0," +
                "    originalText text," +
                "    time         text" +
                ");");
    }

    public int update(String tab, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase writ = getWritableDatabase();
        return writ.update(tab, values, whereClause, whereArgs);
    }

    public long instData(String tab, Object o) {
        try {
            SQLiteDatabase writ = getWritableDatabase();
            Class<?> mClass = o.getClass();
            Field[] fields = mClass.getFields();
            ContentValues contentValues = new ContentValues();
            for (Field field : fields) {
                String key = field.getName();
                field.setAccessible(true);
                Object object = field.get(o);
                if (object instanceof String text) {
                    contentValues.put(key, text);
                } else if (object instanceof Integer integer) {
                    contentValues.put(key, integer);
                }
            }
            return writ.insert(tab, null, contentValues);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 判断不存在数据
     *
     * @param sql SQL命令
     * @param str 字符串
     * @return
     */
    public boolean isEmpty(String sql, String[] str) {
        SQLiteDatabase read = getReadableDatabase();
        Cursor cursor = read.rawQuery(sql, str);
        int count = cursor.getCount();
        cursor.close();
        read.close();
        return count == 0;
    }


    public JSONArray getResults(String searchQuery, String[] userTable) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(searchQuery, userTable);
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if (cursor.getString(i) != null) {
                            //Log.d("userTable", cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        // Log.d("userTable", e.getMessage()  );
                    }
                }
            }
            resultSet.add(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        return resultSet;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
