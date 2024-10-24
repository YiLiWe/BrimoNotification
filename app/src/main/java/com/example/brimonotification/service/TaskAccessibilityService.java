package com.example.brimonotification.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

////adb shell pm grant com.example.brimonotification android.permission.WRITE_SECURE_SETTINGS
//重复开启无障碍
public class TaskAccessibilityService extends Service implements Handler.Callback {
    private Handler handler = new Handler(Looper.getMainLooper(), this);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler.postDelayed(this::enableAccessibilityService, 5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler = null;
    }

    private void enableAccessibilityService() {
        disableAccessibilityService(this);
        enableAccessibilityService(this);
        if (handler == null) return;
        handler.postDelayed(this::enableAccessibilityService, 5000);
    }
//adb shell pm grant com.example.brimonotification android.permission.WRITE_SECURE_SETTINGS
    //开启
    public void enableAccessibilityService(Context context) {
        try {
            // 获取当前已启用的无障碍服务
            String currentServices = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            );

            // 如果已有服务，追加分隔符
            if (currentServices != null && !currentServices.isEmpty()) {
                currentServices += ":";
            }

            // 追加新的服务
            currentServices += "com.example.brimonotification/com.example.brimonotification.service.NotionalPoolingAccessibilityService";

            // 更新启用的无障碍服务
            Settings.Secure.putString(
                    context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
                    currentServices
            );

            // 启用无障碍服务
            Settings.Secure.putString(
                    context.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED,
                    "1"
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //关闭无障碍
    public void disableAccessibilityService(Context context) {
        try {
            // 获取当前已启用的无障碍服务
            String currentServices = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            );

            if (currentServices != null) {
                // 移除指定服务
                String serviceToRemove = "com.example.brimonotification/com.example.brimonotification.service.NotionalPoolingAccessibilityService";
                String updatedServices = currentServices.replace(serviceToRemove, "").replace("::", ":").replaceAll("^:|:$", "");

                // 更新启用的无障碍服务
                Settings.Secure.putString(
                        context.getContentResolver(),
                        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
                        updatedServices
                );

                // 如果没有服务被启用，则禁用无障碍
                if (updatedServices.isEmpty()) {
                    Settings.Secure.putString(
                            context.getContentResolver(),
                            Settings.Secure.ACCESSIBILITY_ENABLED,
                            "0"
                    );
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        return false;
    }
}
