package com.example.brimonotification.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;

import com.example.brimonotification.bean.NotionalPoolingBean;
import com.example.brimonotification.runnable.NotionalPoolingDataRunnable;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 自动归集
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NotionalPoolingAccessibilityService extends AccessibilityService {
    private static final String TAG = "NotionalPoolingAccessibilityService";
    private final String pass = "Tang443312@";//登录密码
    private String amount = "1000";//余额
    private NotionalPoolingBean poolingBean = null;//转账信息
    private boolean isRun = true; // Ensures thread-safe access
    private final Timer timer = new Timer();

    private final long NotionalPoolingTimeMAX = 10000;//获取归集数据集间隔
    private final long POST_DELAY_MS = 20000, GESTURE_DURATION_MS = 1000; // Delay for posting logs

    private AccessibilityNodeInfo nodeInfo = null;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        print("服务启动");
        startData();
    }

    private void print(String msg) {
        Log.d(TAG, msg);
    }

    private void NotionalPooling() {
        if (!isRun) return;
        if (poolingBean == null && !amount.equals("0")) {//判断数据，金额不为0，才执行
            print("请求归集数据");
            new Thread(new NotionalPoolingDataRunnable(this)).start();
        } else {
            print("归集数据不为空");
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                NotionalPooling();
            }
        }, NotionalPoolingTimeMAX);
    }

    private void startData() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                print("运行");
                simulateSwipeUp();
            }
        }, POST_DELAY_MS);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                NotionalPooling();
            }
        }, NotionalPoolingTimeMAX);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            this.nodeInfo = nodeInfo;
        }
        if (this.nodeInfo == null) return;
        handleLogin(this.nodeInfo);
        handleAmount(this.nodeInfo);
        handleTransfer(this.nodeInfo);
    }

    /**
     * 处理转账转账
     *
     * @param nodeInfo
     */
    private void handleTransfer(AccessibilityNodeInfo nodeInfo) {
        if (poolingBean == null) return;
        ClickTransfer(nodeInfo);//首页
        ClickTambahPenerimaBaru(nodeInfo);
        if (!nodeInfo.findAccessibilityNodeInfosByText("Penerima Baru").isEmpty()) {//输入账号信息
            ClickSelectBank(nodeInfo);
        } else if (!nodeInfo.findAccessibilityNodeInfosByText("Masukkan Nominal").isEmpty()) {//输入金额
            editNominal(nodeInfo);
        }
    }

    /**
     * 输入金额
     *
     * @param nodeInfo
     */
    private void editNominal(AccessibilityNodeInfo nodeInfo) {
        List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByViewId("id.co.bri.brimo:id/et_nominal");
        for (AccessibilityNodeInfo bank : nodeInfos) {
            if (bank.getText() == null) continue;
            String text = bank.getText().toString();
            if (!text.equals(poolingBean.getAmount())) {//输入金额
                editText(bank, poolingBean.getAmount());
            } else {//确认转账
                ClickLanjut(nodeInfo);
            }
        }
    }

    /**
     * 选择银行
     *
     * @param nodeInfo
     */
    private void ClickSelectBank(AccessibilityNodeInfo nodeInfo) {
        List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByViewId("id.co.bri.brimo:id/et_bank");
        for (AccessibilityNodeInfo bank : nodeInfos) {
            if (bank.getText() == null) continue;
            String text = bank.getText().toString();
            if (!text.contains(poolingBean.getBank())) {//不存在则点击选择
                bank.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            } else {//输入银行卡
                editAccount(nodeInfo);
            }
        }
        //上面点击，以后输入银行
        editBank(nodeInfo);
    }

    /**
     * 输入银行卡
     *
     * @param nodeInfo
     */
    private void editAccount(AccessibilityNodeInfo nodeInfo) {
        List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByViewId("id.co.bri.brimo:id/et_norek");
        for (AccessibilityNodeInfo nodeInfo1 : nodeInfos) {
            if (nodeInfo1.getText() == null) continue;
            String text = nodeInfo1.getText().toString();
            if (!text.equals(poolingBean.getAccount())) {//未输入银行号
                editText(nodeInfo1, poolingBean.getAccount());
            } else {//以输入银行卡
                ClickLanjut(nodeInfo);
            }
        }
    }

    /**
     * 确认信息
     *
     * @param nodeInfo
     */
    private void ClickLanjut(AccessibilityNodeInfo nodeInfo) {
        ClickNodeInfo(nodeInfo, "id.co.bri.brimo:id/btn_lanjut");
    }

    /**
     * 输入银行
     *
     * @param nodeInfo
     */
    private void editBank(AccessibilityNodeInfo nodeInfo) {
        List<AccessibilityNodeInfo> searchView = nodeInfo.findAccessibilityNodeInfosByViewId("id.co.bri.brimo:id/searchView");
        for (AccessibilityNodeInfo search : searchView) {
            if (search.getText() == null) continue;
            String text = search.getText().toString();
            if (text.contains(poolingBean.getBank())) {//存在则选择
                optionBank(nodeInfo);
            } else {
                editText(search, poolingBean.getBank());
            }
        }
    }

    /**
     * 选择框，选择
     *
     * @param nodeInfo
     */
    private void optionBank(AccessibilityNodeInfo nodeInfo) {
        List<AccessibilityNodeInfo> option_bank = nodeInfo.findAccessibilityNodeInfosByViewId("id.co.bri.brimo:id/tv_option_name");
        for (AccessibilityNodeInfo bank : option_bank) {
            if (bank.getText() == null) continue;
            String text = bank.getText().toString();
            if (text.contains(poolingBean.getBank())) {//不存在则点击选择
                boolean is = bank.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                print(String.format("选择银行:%s|选择状态:%s", text, is));
            }
        }
    }

    /**
     * 点击进入转账
     *
     * @param nodeInfo
     */
    private void ClickTambahPenerimaBaru(AccessibilityNodeInfo nodeInfo) {
        ClickNodeInfo(nodeInfo, "id.co.bri.brimo:id/btnSubmit");
    }

    /**
     * 点击转账
     *
     * @param nodeInfo
     */
    private void ClickTransfer(AccessibilityNodeInfo nodeInfo) {
        List<AccessibilityNodeInfo> transfers = nodeInfo.findAccessibilityNodeInfosByText("Transfer");
        for (AccessibilityNodeInfo transfer : transfers) {
            if (transfer.getViewIdResourceName() == null) continue;
            if (transfer.getViewIdResourceName().equals("id.co.bri.brimo:id/namaMenu")) {
                AccessibilityNodeInfo parent = transfer.getParent();
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }


    /**
     * 获取余额
     *
     * @param nodeInfo
     */
    private void handleAmount(AccessibilityNodeInfo nodeInfo) {
        String text = getText(nodeInfo, "id.co.bri.brimo:id/total_saldo_ib");
        if (text == null) return;
        if (text.startsWith("Rp")) {
            amount = getInteger(text);
        }
    }

    /**
     * 处理登录
     *
     * @param nodeInfo
     */
    private void handleLogin(AccessibilityNodeInfo nodeInfo) {
        //点击首页登录
        ClickNodeInfo(nodeInfo, "id.co.bri.brimo:id/btn_login");
        EditNodeInfo(nodeInfo, "id.co.bri.brimo:id/et_password", pass);
        ClickNodeInfo(nodeInfo, "id.co.bri.brimo:id/button_login");
    }

    /**
     * 获取文本
     *
     * @param nodeInfo
     * @param id
     * @return
     */
    private String getText(AccessibilityNodeInfo nodeInfo, String id) {
        List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByViewId(id);
        for (AccessibilityNodeInfo pass : nodeInfos) {
            if (pass.getText() == null) continue;
            return pass.getText().toString();
        }
        return null;
    }

    /**
     * 提取纯数字
     *
     * @param input
     * @return
     */
    private String getInteger(String input) {
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(input);
        StringBuilder digitsOnly = new StringBuilder();
        while (matcher.find()) {
            // 获取匹配的数字字符
            digitsOnly.append(matcher.group());
        }
        return digitsOnly.toString();
    }

    /**
     * 输入内容
     *
     * @param nodeInfo
     * @param id
     * @param text
     */
    private boolean EditNodeInfo(AccessibilityNodeInfo nodeInfo, String id, String text) {
        List<AccessibilityNodeInfo> Passwords = nodeInfo.findAccessibilityNodeInfosByViewId(id);
        for (AccessibilityNodeInfo pass : Passwords) {
            if (pass.getText() == null) continue;
            String string = pass.getText().toString();
            if (!string.equals(text)) {
                editText(pass, text);
                print(String.format("输入登录密码:%s|ID:%s", text, id));
                return true;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 模拟点击
     *
     * @param nodeInfo
     * @param id
     */
    private void ClickNodeInfo(AccessibilityNodeInfo nodeInfo, String id) {
        List<AccessibilityNodeInfo> logins = nodeInfo.findAccessibilityNodeInfosByViewId(id);
        for (AccessibilityNodeInfo login : logins) {
            boolean is = login.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            print(String.format("点击ID:%s|点击状态:%s", id, is));
        }
    }

    /**
     * 输入文字
     *
     * @param nodeInfo
     * @param msg
     */
    private void editText(AccessibilityNodeInfo nodeInfo, String msg) {
        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        Bundle arguments = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, msg);
        boolean is = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        print(String.format("输入信息:%s|输入状态:%s", msg, is));
    }

    private void simulateSwipeUp() {
        if (poolingBean == null) {
            Path path = new Path();
            path.moveTo(500, 1000);
            path.lineTo(500, 1500);
            GestureDescription.StrokeDescription strokeDescription = new GestureDescription.StrokeDescription(path, 0, GESTURE_DURATION_MS);
            GestureDescription.Builder builder = new GestureDescription.Builder();
            builder.addStroke(strokeDescription);
            dispatchGesture(builder.build(), null, null);
            print("模拟滑动");
        } else {
            print("停止滑动");
        }
        if (isRun) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    simulateSwipeUp();
                }
            }, POST_DELAY_MS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRun = false;
        poolingBean = null;
        amount = "0";
        Log.d("详细", "关闭");
    }


    @Override
    public void onInterrupt() {

    }
}
