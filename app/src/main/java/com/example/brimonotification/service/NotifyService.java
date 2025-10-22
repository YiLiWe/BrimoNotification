package com.example.brimonotification.service;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.fastjson2.JSON;
import com.example.brimonotification.bean.NotificationBean;
import com.example.brimonotification.databinding.LayoutLogBinding;
import com.example.brimonotification.room.AppDatabase;
import com.example.brimonotification.room.dao.BillDao;
import com.example.brimonotification.room.entity.BillEntity;
import com.example.brimonotification.utils.Logs;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Description 监听通知服务
 * @Author 不一样的风景
 * @Time 2024/11/2 17:13
 */
public class NotifyService extends NotificationListenerService {
    public final String TAG = "NotifyService";
    private final String BRImo = "id.co.bri.brimo";//银行
    private String url, card;
    private NotifyService.LogWindow logWindow;

    @Override
    public void onCreate() {
        super.onCreate();
        logWindow = new LogWindow(this);
        SharedPreferences sharedPreferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        url = sharedPreferences.getString("url", "");
        card = sharedPreferences.getString("card", "");
    }

    /**
     * 发布通知
     *
     * @param sbn 状态栏通知
     */
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        logWindow.print("收到通知：" + sbn.getPackageName());
        if (sbn.getPackageName().equals("id.co.bri.brimo") || sbn.getPackageName().equals("com.example.brimonotification")) {
            try {
                handleData(sbn);
            } catch (Throwable e) {
                logWindow.printA("处理通知失败:" + e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logWindow.destroy();
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
        logWindow.printA("收到通知:" + msgContent);
        //消息时间
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE).format(new Date(sbn.getPostTime()));
        handleText(msgContent, time);
    }


    private void handleText(String msgContent, String time) {
        if (msgContent.isEmpty()) {
            return;
        }
        if (!msgContent.startsWith("Sobat BRI!")) return;
        NotificationBean notificationBean = getNotificationBean(msgContent);
        notificationBean.setOriginalText(msgContent);
        notificationBean.setNoticeTime(time);

        Logs.printA(JSON.toJSONString(notificationBean));

        BillEntity billEntity = new BillEntity();
        billEntity.setAccount(notificationBean.getAccount());
        billEntity.setAmount(notificationBean.getAmount());
        billEntity.setPayerName(notificationBean.getPayerName());
        billEntity.setTime(notificationBean.getTime());
        billEntity.setNoticeTime(notificationBean.getNoticeTime());
        billEntity.setMsgContent(notificationBean.getOriginalText());
        billEntity.setState(2);
        billEntity.setTime(time);

        postData(billEntity);
        postDataAll();
    }


    //上传历史未成功的订单
    private void postDataAll() {
        new Thread(() -> {
            AppDatabase appDatabase = AppDatabase.getInstance(NotifyService.this);
            BillDao billDao = appDatabase.billDao();
            List<BillEntity> billEntities = billDao.queryByState(10, 0, 0);
            int count = 0;
            int errorCount = 0;
            for (BillEntity billEntity : billEntities) {
                billDao.updateStateById(billEntity.getUid(), 2);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(String.format("%sapi/app/confirmReceiptSuccess?amount=%s&payerName=%s&cardNumber=%s", url, billEntity.getAmount(), billEntity.getPayerName(), card))
                        .build();
                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        billDao.updateStateById(billEntity.getUid(), 1);
                        count++;
                    }
                } catch (IOException e) {
                    errorCount++;
                    billDao.updateStateById(billEntity.getUid(), 0);
                    e.printStackTrace();
                }
            }
            logWindow.print("历史订单上传:" + count + "条成功," + errorCount + "条失败");
        }).start();
    }

    //提交数据
    private void postData(BillEntity billEntity) {
        logWindow.printA("新订单上传:" + billEntity.getAmount());
        new Thread(() -> {
            AppDatabase appDatabase = AppDatabase.getInstance(this);
            BillDao billDao = appDatabase.billDao();
            long id = billDao.insert(billEntity);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(String.format("%sapi/app/confirmReceiptSuccess?amount=%s&payerName=%s&cardNumber=%s", url, billEntity.getAmount(), billEntity.getPayerName(), card))
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    logWindow.print("新订单上传:" + billEntity.getAmount() + "成功");
                    billDao.updateStateById(id, 1);
                } else {
                    billDao.updateStateById(id, 0);
                    logWindow.print("新订单上传:" + billEntity.getAmount() + "失败");
                }
            } catch (IOException e) {
                billDao.updateStateById(id, 0);
                logWindow.print("新订单上传:" + billEntity.getAmount() + "失败");
                e.printStackTrace();
            }
        }).start();
    }

    private NotificationBean getNotificationBean(String input) {
        NotificationBean notificationBean = new NotificationBean();
        // 正则表达式
        String amountRegex = "Rp([\\d,.]+)";
        String accountRegex = "rekening (\\d+)";
        String dateTimeRegex = "(\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2})";
        String nameRegex = "(?<=KET\\.:)(.*)";

        // 提取金额
        Pattern amountPattern = Pattern.compile(amountRegex);
        Matcher amountMatcher = amountPattern.matcher(input);
        if (amountMatcher.find()) {
            String amount = amountMatcher.group(1);
            if (amount != null) {
                Logs.printA("金额: " + amount);
                amount = amount.replace(".", "")
                        .replace(",", "");
                notificationBean.setAmount(amount);
            }
        }

        // 提取银行账户
        Pattern accountPattern = Pattern.compile(accountRegex);
        Matcher accountMatcher = accountPattern.matcher(input);
        if (accountMatcher.find()) {
            String account = accountMatcher.group(1);
            notificationBean.setAccount(account);
        }

        // 提取日期和时间
        Pattern dateTimePattern = Pattern.compile(dateTimeRegex);
        Matcher dateTimeMatcher = dateTimePattern.matcher(input);
        if (dateTimeMatcher.find()) {
            String dateTime = dateTimeMatcher.group(1);
            notificationBean.setTime(dateTime);
        }

        notificationBean.setPayerName(input);
        // 提取名字
        /*Pattern namePattern = Pattern.compile(nameRegex);
        Matcher nameMatcher = namePattern.matcher(input);
        if (nameMatcher.find()) {
            String name = nameMatcher.group(1);
            notificationBean.setPayerName(name);
            appText("名字: " + name);
        }*/
        return notificationBean;
    }

    /**
     * 监听断开
     */
    @Override
    public void onListenerDisconnected() {
        // 通知侦听器断开连接 - 请求重新绑定
        requestRebind(new ComponentName(this, NotificationListenerService.class));
    }


    public static class LogWindow {
        private final Handler handler = new Handler(Looper.getMainLooper());
        private final NotifyService service;
        private final LayoutLogBinding binding;
        private WindowManager windowManager;

        public LogWindow(NotifyService service) {
            this.service = service;
            this.binding = LayoutLogBinding.inflate(LayoutInflater.from(service));
            init();
        }

        public void print(String str) {
            handler.post(() -> printA(str));
        }

        private void printA(String str) {
            // 获取当前文本
            String currentText = binding.text.getText().toString();
            String[] lines = currentText.split("\n");

            // 检查最新内容是否已经存在
            boolean isDuplicate = false;
            for (String line : lines) {
                if (line.equals(str)) {
                    isDuplicate = true;
                    break;
                }
            }
            // 如果内容不存在，才追加
            if (!isDuplicate) {
                // 如果行数超过 20，移除最早的行
                if (lines.length >= 10) {
                    StringBuilder newText = new StringBuilder();
                    // 保留最后 19 行
                    for (int i = lines.length - 9; i < lines.length; i++) {
                        newText.append(lines[i]);
                    }
                    binding.text.setText(newText.toString());
                }
                // 追加新内容并滚动到底部
                binding.text.append("\n" + getCurrentDate() + ": " + str);
                binding.scroll.post(() -> binding.scroll.fullScroll(View.FOCUS_DOWN));
            }
        }

        public static String getCurrentDate() {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-ss", Locale.getDefault());
            return sdf.format(new java.util.Date());
        }

        private void init() {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    dpToPx(service, 300),
                    dpToPx(service, 100),
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |  // 关键：悬浮窗不获取焦点
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, // 关键：悬浮窗不可触摸（穿透点击）
                    PixelFormat.TRANSLUCENT
            );
            params.gravity = Gravity.TOP | Gravity.END;
            windowManager = (WindowManager) service.getSystemService(Context.WINDOW_SERVICE);
            windowManager.addView(binding.getRoot(), params);
        }

        public void destroy() {
            windowManager.removeView(binding.getRoot());
        }

        public int dpToPx(Context context, float dp) {
            float density = context.getResources().getDisplayMetrics().density;
            return (int) (dp * density + 0.5f); // 四舍五入
        }
    }

}