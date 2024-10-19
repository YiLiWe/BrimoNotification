package com.example.brimonotification.servic;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.example.brimonotification.window.NotifyWindow;

import java.sql.Date;
import java.util.Locale;

public class NotifyService extends NotificationListenerService {
    public final String TAG = "NotifyService";
    private final String BRImo = "id.co.bri.brimo";//银行

    @Override
    public void onCreate() {
        super.onCreate();
        NotifyWindow.getNotifyWindow(this).start();
    }


    /**
     * 发布通知
     *
     * @param sbn 状态栏通知
     */
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn.getPackageName().equals(BRImo)) {
            handleData(sbn);
        }
    }

    public void handleData(StatusBarNotification sbn) {
        String msgContent = "";
        if (sbn.getNotification().tickerText != null) {
            msgContent = sbn.getNotification().tickerText.toString();
        }
        //消息时间
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE).format(new Date(sbn.getPostTime()));
        NotifyWindow.getNotifyWindow(this).appText(String.format("内容：%s|时间：%s", msgContent, time));
    }

    /**
     * 通知已删除
     *
     * @param sbn 状态栏通知
     */
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (sbn.getPackageName().equals(BRImo)) {
            Log.d(TAG, "移除账单");
        }
    }

    /**
     * 监听断开
     */
    @Override
    public void onListenerDisconnected() {
        // 通知侦听器断开连接 - 请求重新绑定
        requestRebind(new ComponentName(this, NotificationListenerService.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotifyWindow.getNotifyWindow(this).onDestroy();
    }

    /**
     * @param context 反正第二次启动失败
     */
    public static void toggleNotificationListenerService(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context, NotifyService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName(context, NotifyService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}