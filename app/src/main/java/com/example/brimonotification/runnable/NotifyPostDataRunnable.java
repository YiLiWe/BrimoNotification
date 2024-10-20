package com.example.brimonotification.runnable;

import com.example.brimonotification.bean.NotificationBean;

import java.io.IOException;

import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Data
public class NotifyPostDataRunnable implements Runnable {
    private final NotificationBean bean;
    private final long id;
    private String BASE_URL = "https://admin.tynpay.site/app/confirmReceiptSuccess?amount=%s&payerName=%s";
    private OnMessage onMessage;

    @Override
    public void run() {
        String url = String.format(BASE_URL, bean.getAmount(), bean.getPayerName());
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String text = response.body().string(); // Be careful with this if the body is large
                if (onMessage != null) {
                    onMessage.onMessage(text, id);
                }
            }
        } catch (IOException e) {
            if (onMessage != null) {
                onMessage.onMessage("上传失败", -1);
            }
        }
    }

    public interface OnMessage {
        void onMessage(String msg, long id);
    }
}
