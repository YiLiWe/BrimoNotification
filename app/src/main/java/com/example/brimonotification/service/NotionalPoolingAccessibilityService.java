package com.example.brimonotification.service;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * 自动归集
 */
public class NotionalPoolingAccessibilityService extends AccessibilityService {
    //2024-10-20 08:41:43.532 20051-20051 控件信息                    com.example.brimonotification        D  android.view.accessibility.AccessibilityNodeInfo@5f4ff; boundsInParent: Rect(0, 0 - 656, 96); boundsInScreen: Rect(32, 1261 - 688, 1357); boundsInWindow: Rect(32, 1261 - 688, 1357); packageName: id.co.bri.brimo; className: android.widget.EditText; text: Password; error: null; maxTextLength: -1; stateDescription: null; contentDescription: null; tooltipText: null; containerTitle: null; viewIdResName: id.co.bri.brimo:id/et_password; uniqueId: null; checkable: false; checked: false; focusable: true; focused: false; selected: false; clickable: true; longClickable: true; contextClickable: false; enabled: true; password: true; scrollable: false; importantForAccessibility: true; visible: true; actions: [AccessibilityAction: ACTION_FOCUS - null, AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_CLICK - null, AccessibilityAction: ACTION_LONG_CLICK - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_SET_TEXT - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]; isTextSelectable: false
    //2024-10-20 08:41:43.535 20051-20051 控件信息                    com.example.brimonotification        D  android.view.accessibility.AccessibilityNodeInfo@616c8; boundsInParent: Rect(0, 0 - 656, 96); boundsInScreen: Rect(32, 1405 - 688, 1501); boundsInWindow: Rect(32, 1405 - 688, 1501); packageName: id.co.bri.brimo; className: android.widget.Button; text: Login; error: null; maxTextLength: -1; stateDescription: null; contentDescription: null; tooltipText: null; containerTitle: null; viewIdResName: id.co.bri.brimo:id/button_login; uniqueId: null; checkable: false; checked: false; focusable: true; focused: false; selected: false; clickable: true; longClickable: false; contextClickable: false; enabled: false; password: false; scrollable: false; importantForAccessibility: true; visible: true; actions: [AccessibilityAction: ACTION_FOCUS - null, AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_NEXT_AT_MOVEMENT_GRANULARITY - null, AccessibilityAction: ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY - null, AccessibilityAction: ACTION_SET_SELECTION - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]; isTextSelectable: false
    //id.co.bri.brimo:id/btn_login

    private static final String TAG = "NotionalPoolingAccessibilityService";
    private final String pass = "Coc135689";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d("详细", "启动");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) return;
        handleLogin(nodeInfo);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("详细", "关闭");
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
     * 输入内容
     *
     * @param nodeInfo
     * @param id
     * @param text
     */
    private void EditNodeInfo(AccessibilityNodeInfo nodeInfo, String id, String text) {
        List<AccessibilityNodeInfo> Passwords = nodeInfo.findAccessibilityNodeInfosByViewId(id);
        for (AccessibilityNodeInfo pass : Passwords) {
            String string = pass.getText().toString();
            if (!string.equals(text)) {
                editText(pass, text);
            }
        }
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
            Log.d("显示状态", String.valueOf(login.isVisibleToUser()));
            login.performAction(AccessibilityNodeInfo.ACTION_CLICK);
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
        Log.d("输入信息", msg);
        Bundle arguments = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, msg);
        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
    }

    @Override
    public void onInterrupt() {

    }
}
