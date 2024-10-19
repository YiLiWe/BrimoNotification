package com.example.brimonotification.bean;

import lombok.Data;

@Data
public class NotificationBean {
    private long id;
    private String amount;//金额
    private String account;//账号
    private String payerName;//名字
    private String noticeTime;//通知时间
    private int state;//状态 0=未提交 1=提交成功
    private String originalText;//原文
    private String time;//收款时间
}
