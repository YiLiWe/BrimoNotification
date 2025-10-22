package com.example.brimonotification.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;


import com.example.brimonotification.R;
import com.example.brimonotification.utils.bean.NotificationBean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Random;

/**
 * @Description 通知工具类
 * 需要配置权限:
 * <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
 * <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
 * <uses-permission android:name="android.permission.FOREGROUND_SERVICE_REMOTE_MESSAGING" />
 * <p>
 * <service
 * android:name=".NoService"
 * android:foregroundServiceType="remoteMessaging" />
 * @Author 不一样的风景
 * @Time 2024/12/6 19:56
 */
public class NotificationUtil {
    private String CHANNEL_ID = "default_channel"; // 通知渠道的ID
    private String name = "Default Channel";//渠道名
    private String description = "This is the default notification channel.";//详细
    private final Context mContext;
    private int pri = NotificationManager.IMPORTANCE_HIGH;
    private int id = 32;
    private int defaults;
    private static int requestCode = 5855;

    /**
     * IMPORTANT_NONE 关闭通知
     * IMPORTANT_MIN 开启通知，不弹出，无提示音，状态栏不显示
     * IMPORTANT_LOW 开启通知，不弹出，无提示音，状态栏显示
     * IMPORTANT_DEFAULT 开启通知，不会弹出，发出提示音，状态栏显示
     * IMPORTANT_HIGH 开启通知，会弹出，发出提示音，状态栏显示
     */
    @IntDef({NotificationManager.IMPORTANCE_MAX,
            NotificationManager.IMPORTANCE_DEFAULT,
            NotificationManager.IMPORTANCE_HIGH,
            NotificationManager.IMPORTANCE_MIN,
            NotificationManager.IMPORTANCE_UNSPECIFIED,
            NotificationManager.IMPORTANCE_NONE,
            NotificationManager.IMPORTANCE_LOW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Pri {
    }

    /**
     * Notification.DEFAULT_SOUND：表示通知默认播放声音。
     * Notification.DEFAULT_VIBRATE：表示通知默认震动。
     * Notification.DEFAULT_ALL：表示通知默认包括声音、震动和其他设置。
     */
    @IntDef({Notification.DEFAULT_SOUND,
            Notification.DEFAULT_ALL,
            Notification.DEFAULT_VIBRATE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Defaults {
    }

    public static void setRequestCode(int requestCode) {
        NotificationUtil.requestCode = requestCode;
    }

    /**
     * @Description 启动类
     * 需要在5分钟内发送startForeground通知
     * @code 1
     * @Author 不一样的风景
     * @Time 2024/12/7 8:21
     */
    public static void startForegroundService(Context context, Class<?> service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, service));
        } else {
            context.startService(new Intent(context, service));
        }
    }

    public NotificationUtil setDefaults(@Defaults int defaults) {
        this.defaults = defaults;
        return this;
    }

    public NotificationUtil setDescription(String description) {
        this.description = description;
        return this;
    }

    public NotificationUtil setName(String name) {
        this.name = name;
        return this;
    }

    public NotificationUtil setCHANNEL_ID(String CHANNEL_ID) {
        this.CHANNEL_ID = CHANNEL_ID;
        return this;
    }

    public NotificationUtil setPri(@Pri int pri) {
        this.pri = pri;
        return this;
    }

    public NotificationUtil setId(int id) {
        this.id = id;
        return this;
    }

    public static int getRandom() {
        Random random = new Random();
        // 生成一个随机整数
        return random.nextInt();
    }

    public NotificationUtil(Context context) {
        this.mContext = context;
        createNotificationChannel();
    }

    /**
     * @Description 发送通知
     * @code 1
     * @Author 不一样的风景
     * @Time 2024/12/7 8:39
     */
    public NotificationUtil sendNotification(NotificationBean bean) {
        Notification notification = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher) // 通知图标
                .setContentTitle(bean.getTitle()) // 通知标题
                .setContentText(bean.getContext()) // 通知内容
                .setDefaults(defaults)
                .setPriority(pri) // 设置优先级
                .setAutoCancel(false) // 点击通知后自动取消
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(id, notification); // 1 是通知的ID，可以用于更新或取消通知
        }
        return this;
    }

    /**
     * @Description 权限申请和判断
     * @code 1
     * @Author 不一样的风景
     * @Time 2024/12/7 8:18
     */
    @SuppressLint("InlinedApi")
    public static boolean requestPermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.POST_NOTIFICATIONS}, requestCode);
            return false;
        }
        return true;
    }

    /**
     * @Description 发送通知
     * @code 1
     * @Author 不一样的风景
     * @Time 2024/12/6 22:02
     */
    public NotificationUtil sendNotification(Notification notification) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(id, notification); // 1 是通知的ID，可以用于更新或取消通知
        }
        return this;
    }

    /**
     * @Description 前台服务通知
     * @code 1
     * @Author 不一样的风景
     * @Time 2024/12/6 22:01
     */
    public NotificationUtil startForeground(Service service) {
        Notification notification = new NotificationCompat.Builder(mContext, CHANNEL_ID).build();
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.FOREGROUND_SERVICE_REMOTE_MESSAGING) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                service.startForeground(id, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST);
            } else {
                service.startForeground(id, notification);
            }
        }
        return this;
    }

    /**
     * @Description 创建通知
     * @code 1
     * @Author 不一样的风景
     * @Time 2024/12/6 21:44
     */
    public NotificationUtil createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        return this;
    }

    private static NotificationUtil notificationUtil;

    public static NotificationUtil getInstance(Context context) {
        if (notificationUtil == null) {
            notificationUtil = new NotificationUtil(context);
        }
        return notificationUtil;
    }
}
