package com.example.brimonotification.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson2.JSON;

import lombok.Data;

public class NotionalPoolingSharedPreferencesUtil {
    private final String TAG = "NotionalPoolingSharedPr";
    private final Context context;
    private SharedPreferences sharedPreferences;

    public NotionalPoolingSharedPreferencesUtil(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(NotionalPoolingSharedPreferencesUtil.class.getName(), Context.MODE_PRIVATE);
    }

    public void savaString(String key, String value) {
        sharedPreferences.edit()
                .putString(key, value)
                .apply();
    }

    public String getString(String key, String value) {
        return sharedPreferences.getString(key, value);
    }

    public void savaObject(String key, Object value) {
        sharedPreferences.edit()
                .putString(key, JSON.toJSONString(value))
                .apply();
    }

    public <T> T getObject(String key, Class<T> Class) {
        String value = sharedPreferences.getString(key, null);
        if (value == null) return null;
        return JSON.to(Class, value);
    }
}
