package com.example.brimonotification.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bill")
public class BillEntity {
    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // 主键
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    public long uid;

    //金额
    @ColumnInfo(name = "amount")
    private String amount;

    //姓名
    @ColumnInfo(name = "payerName")
    private String payerName;//名字

    //账号
    @ColumnInfo(name = "account")
    private String account;//账号

    @ColumnInfo(name = "noticeTime")
    private String noticeTime;//通知时间

    @ColumnInfo(name = "state")
    private int state;//状态 0=提交失败 1=提交成功 2=处理中

    @ColumnInfo(name = "msgContent")
    private String msgContent;//消息内容

    @ColumnInfo(name = "time")
    private String time;//收款时间
}
