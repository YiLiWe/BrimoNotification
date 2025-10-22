package com.example.brimonotification.utils.bean;

/**
 * @Description
 * @Author 不一样的风景
 * @Time 2024/12/6 19:57
 */
public class NotificationBean {
    private String title;
    private String context;

    public NotificationBean(String title, String context) {
        this.title = title;
        this.context = context;
    }

    public String getTitle() {
        return title;
    }

    public String getContext() {
        return context;
    }
}
