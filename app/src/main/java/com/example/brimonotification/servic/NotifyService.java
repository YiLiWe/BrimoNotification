package com.example.brimonotification.servic;

import android.app.Notification;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import androidx.annotation.NonNull;

import com.example.brimonotification.bean.NotificationBean;
import com.example.brimonotification.helper.MyDBOpenHelper;
import com.example.brimonotification.runnable.PostDataRunnable;
import com.example.brimonotification.window.NotifyWindow;

import java.sql.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotifyService extends NotificationListenerService implements Handler.Callback {
    public final String TAG = "NotifyService";
    private final String BRImo = "id.co.bri.brimo";//银行
    private MyDBOpenHelper helper;
    private final Handler handler = new Handler(Looper.getMainLooper(), this);

    @Override
    public void onCreate() {
        super.onCreate();
        helper = new MyDBOpenHelper(this);
        if (Settings.canDrawOverlays(this)) {
            NotifyWindow.getNotifyWindow(this).start();
            appText("服务开启成功");
        }
    }

    /**
     * 发布通知
     *
     * @param sbn 状态栏通知
     */
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (!sbn.getPackageName().equals(BRImo)) return;
        handleData(sbn);
    }

    public void handleData(StatusBarNotification sbn) {
        String msgContent = "";
        if (sbn.getNotification().tickerText != null) {
            msgContent = sbn.getNotification().tickerText.toString();
        }
        if (msgContent.isEmpty()) {
            Bundle bundle = sbn.getNotification().extras;
            msgContent = bundle.getString(Notification.EXTRA_TEXT, "");
        }
        //消息时间
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE).format(new Date(sbn.getPostTime()));
        handleText(msgContent, time);
    }

    public void appText(String msg) {
        if (Settings.canDrawOverlays(this)) {
            NotifyWindow.getNotifyWindow(this).appText(msg + "\n");
        }
    }

    private void handleText(String msgContent, String time) {
        if (msgContent.isEmpty()) {
            appText("内容为空");
            return;
        }
        if (!msgContent.startsWith("Sobat BRI!")) return;
        NotificationBean notificationBean = getNotificationBean(msgContent);
        appText("\n");
        notificationBean.setOriginalText(msgContent);
        notificationBean.setNoticeTime(time);
        long id = helper.instData("notification", notificationBean);
        PostDataRunnable postDataRunnable = new PostDataRunnable(notificationBean, id);
        postDataRunnable.setOnMessage(this::onMessage);
        new Thread(postDataRunnable).start();
    }

    private void onMessage(String msg, long id) {
        if (id == -1) {
            Message message = new Message();
            message.obj = "上传失败";
            handler.sendMessage(message);
        } else {//记录
            ContentValues values = new ContentValues();
            values.put("state", 1);
            helper.update("notification", values, "id=?", new String[]{String.valueOf(id)});
        }
    }

    private NotificationBean getNotificationBean(String input) {
        NotificationBean notificationBean = new NotificationBean();
        // 正则表达式
        String amountRegex = "Rp([\\d,.]+)";
        String accountRegex = "rekening (\\d+)";
        String dateTimeRegex = "(\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2})";
        String nameRegex = "NBMB ([A-Za-z ]+) TO";

        // 提取金额
        Pattern amountPattern = Pattern.compile(amountRegex);
        Matcher amountMatcher = amountPattern.matcher(input);
        if (amountMatcher.find()) {
            String amount = amountMatcher.group(1);
            if (amount != null) {
                amount = amount.replace(".", "")
                        .replace(",", "");
                notificationBean.setAmount(amount);
                appText("金额: " + amount);
            }
        }

        // 提取银行账户
        Pattern accountPattern = Pattern.compile(accountRegex);
        Matcher accountMatcher = accountPattern.matcher(input);
        if (accountMatcher.find()) {
            String account = accountMatcher.group(1);
            notificationBean.setAccount(account);
            appText("银行账户: " + account);
        }

        // 提取日期和时间
        Pattern dateTimePattern = Pattern.compile(dateTimeRegex);
        Matcher dateTimeMatcher = dateTimePattern.matcher(input);
        if (dateTimeMatcher.find()) {
            String dateTime = dateTimeMatcher.group(1);
            notificationBean.setTime(dateTime);
            appText("收款时间: " + dateTime);
        }

        // 提取名字
        Pattern namePattern = Pattern.compile(nameRegex);
        Matcher nameMatcher = namePattern.matcher(input);
        if (nameMatcher.find()) {
            String name = nameMatcher.group(1);
            notificationBean.setPayerName(name);
            appText("名字: " + name);
        }
        return notificationBean;
    }

    /**
     * 监听断开
     */
    @Override
    public void onListenerDisconnected() {
        appText("服务重启");
        // 通知侦听器断开连接 - 请求重新绑定
        requestRebind(new ComponentName(this, NotificationListenerService.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.close();
        if (Settings.canDrawOverlays(this)) {
            NotifyWindow.getNotifyWindow(this).onDestroy();
        }
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

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (msg.what == 0 && msg.obj instanceof String text) {
            appText(text);
        }
        return false;
    }
}