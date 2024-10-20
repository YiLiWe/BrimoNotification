package com.example.brimonotification.service;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.brimonotification.utils.AccessibilityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动归集
 */
public class NotionalPoolingAccessibilityService extends AccessibilityService {
    //2024-10-20 08:41:43.532 20051-20051 控件信息                    com.example.brimonotification        D  android.view.accessibility.AccessibilityNodeInfo@5f4ff; boundsInParent: Rect(0, 0 - 656, 96); boundsInScreen: Rect(32, 1261 - 688, 1357); boundsInWindow: Rect(32, 1261 - 688, 1357); packageName: id.co.bri.brimo; className: android.widget.EditText; text: Password; error: null; maxTextLength: -1; stateDescription: null; contentDescription: null; tooltipText: null; containerTitle: null; viewIdResName: id.co.bri.brimo:id/et_password; uniqueId: null; checkable: false; checked: false; focusable: true; focused: false; selected: false; clickable: true; longClickable: true; contextClickable: false; enabled: true; password: true; scrollable: false; importantForAccessibility: true; visible: true; actions: [AccessibilityAction: ACTION_FOCUS - null, AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_CLICK - null, AccessibilityAction: ACTION_LONG_CLICK - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_SET_TEXT - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]; isTextSelectable: false
    //2024-10-20 08:41:43.535 20051-20051 控件信息                    com.example.brimonotification        D  android.view.accessibility.AccessibilityNodeInfo@616c8; boundsInParent: Rect(0, 0 - 656, 96); boundsInScreen: Rect(32, 1405 - 688, 1501); boundsInWindow: Rect(32, 1405 - 688, 1501); packageName: id.co.bri.brimo; className: android.widget.Button; text: Login; error: null; maxTextLength: -1; stateDescription: null; contentDescription: null; tooltipText: null; containerTitle: null; viewIdResName: id.co.bri.brimo:id/button_login; uniqueId: null; checkable: false; checked: false; focusable: true; focused: false; selected: false; clickable: true; longClickable: false; contextClickable: false; enabled: false; password: false; scrollable: false; importantForAccessibility: true; visible: true; actions: [AccessibilityAction: ACTION_FOCUS - null, AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_NEXT_AT_MOVEMENT_GRANULARITY - null, AccessibilityAction: ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY - null, AccessibilityAction: ACTION_SET_SELECTION - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]; isTextSelectable: false
    //2024-10-20 11:31:19.176  5916-5916  控件信息                    com.example.brimonotification        D  android.view.accessibility.AccessibilityNodeInfo@ef7cf; boundsInParent: Rect(0, 0 - 656, 44); boundsInScreen: Rect(32, 391 - 688, 435); boundsInWindow: Rect(32, 391 - 688, 435); packageName: id.co.bri.brimo; className: android.widget.TextView; text: Hello; error: null; maxTextLength: 60; stateDescription: null; contentDescription: null; tooltipText: null; containerTitle: null; viewIdResName: id.co.bri.brimo:id/tv_dynamic; uniqueId: null; checkable: false; checked: false; focusable: false; focused: false; selected: false; clickable: false; longClickable: false; contextClickable: false; enabled: true; password: false; scrollable: false; importantForAccessibility: true; visible: true; actions: [AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_NEXT_AT_MOVEMENT_GRANULARITY - null, AccessibilityAction: ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY - null, AccessibilityAction: ACTION_SET_SELECTION - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]; isTextSelectable: false

    /**
     * android.view.accessibility.AccessibilityNodeInfo@a847; boundsInParent: Rect(0, 0 - 720, 1612); boundsInScreen: Rect(0, 0 - 720, 1612); boundsInWindow: Rect(0, 0 - 720, 1612); packageName: id.co.bri.brimo; className: android.widget.LinearLayout; text: null; error: null; maxTextLength: -1; stateDescription: null; contentDescription: null; tooltipText: null; containerTitle: null; viewIdResName: null; uniqueId: null; checkable: false; checked: false; focusable: false; focused: false; selected: false; clickable: false; longClickable: false; contextClickable: false; enabled: true; password: false; scrollable: false; importantForAccessibility: false; visible: true; actions: [AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]; isTextSelectable: false
     * 2024-10-20 12:25:59.703 27546-27546 控件信息                    com.example.brimonotification        D  android.view.accessibility.AccessibilityNodeInfo@afc9; boundsInParent: Rect(0, 0 - 720, 1612); boundsInScreen: Rect(0, 0 - 720, 1612); boundsInWindow: Rect(0, 0 - 720, 1612); packageName: id.co.bri.brimo; className: android.widget.FrameLayout; text: null; error: null; maxTextLength: -1; stateDescription: null; contentDescription: null; tooltipText: null; containerTitle: null; viewIdResName: null; uniqueId: null; checkable: false; checked: false; focusable: false; focused: false; selected: false; clickable: false; longClickable: false; contextClickable: false; enabled: true; password: false; scrollable: false; importantForAccessibility: false; visible: true; actions: [AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]; isTextSelectable: false
     * 2024-10-20 12:25:59.704 27546-27546 控件信息                    com.example.brimonotification        D  android.view.accessibility.AccessibilityNodeInfo@b38a; boundsInParent: Rect(0, 0 - 720, 1612); boundsInScreen: Rect(0, 0 - 720, 1612); boundsInWindow: Rect(0, 0 - 720, 1612); packageName: id.co.bri.brimo; className: android.widget.LinearLayout; text: null; error: null; maxTextLength: -1; stateDescription: null; contentDescription: null; tooltipText: null; containerTitle: null; viewIdResName: id.co.bri.brimo:id/action_bar_root; uniqueId: null; checkable: false; checked: false; focusable: false; focused: false; selected: false; clickable: false; longClickable: false; contextClickable: false; enabled: true; password: false; scrollable: false; importantForAccessibility: false; visible: true; actions: [AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]; isTextSelectable: false
     * 2024-10-20 12:25:59.704 27546-27546 控件信息                    com.example.brimonotification        D  android.view.accessibility.AccessibilityNodeInfo@bb0c; boundsInParent: Rect(0, 0 - 720, 1612); boundsInScreen: Rect(0, 0 - 720, 1612); boundsInWindow: Rect(0, 0 - 720, 1612); packageName: id.co.bri.brimo; className: android.widget.FrameLayout; text: null; error: null; maxTextLength: -1; stateDescription: null; contentDescription: null; tooltipText: null; containerTitle: null; viewIdResName: android:id/content; uniqueId: null; checkable: false; checked: false; focusable: false; focused: false; selected: false; clickable: false; longClickable: false; contextClickable: false; enabled: true; password: false; scrollable: false; importantForAccessibility: false; visible: true; actions: [AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]; isTextSelectable: false
     * 2024-10-20 12:25:59.705 27546-27546 控件信息                    com.example.brimonotification        D  android.view.accessibility.AccessibilityNodeInfo@becd; boundsInParent: Rect(0, 0 - 720, 1612); boundsInScreen: Rect(0, 0 - 720, 1612); boundsInWindow: Rect(0, 0 - 720, 1612); packageName: id.co.bri.brimo; className: android.view.ViewGroup; text: null; error: null; maxTextLength: -1; stateDescription: null; contentDescription: null; tooltipText: null; containerTitle: null; viewIdResName: null; uniqueId: null; checkable: false; checked: false; focusable: false; focused: false; selected: false; clickable: false; longClickable: false; contextClickable: false; enabled: true; password: false; scrollable: false; importantForAccessibility: false; visible: true; actions: [AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]; isTextSelectable: false
     * 2024-10-20 12:25:59.706 27546-27546 控件信息                    com.example.brimonotification        D  android.view.accessibility.AccessibilityNodeInfo@fe9e; boundsInParent: Rect(0, 0 - 720, 1382); boundsInScreen: Rect(0, 72 - 720, 1454); boundsInWindow: Rect(0, 72 - 720, 1454); packageName: id.co.bri.brimo; className: android.view.ViewGroup; text: null; error: null; maxTextLength: -1; stateDescription: null; contentDescription: null; tooltipText: null; containerTitle: null; viewIdResName: id.co.bri.brimo:id/content; uniqueId: null; checkable: false; checked: false; focusable: false; focused: false; selected: false; clickable: false; longClickable: false; contextClickable: false; enabled: true; password: false; scrollable: false; importantForAccessibility: true; visible: true; actions: [AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]; isTextSelectable: false
     * 2024-10-20 12:25:59.708 27546-27546 控件信息                    com.example.brimonotification        D  android.view.accessibility.AccessibilityNodeInfo@1025f; boundsInParent: Rect(0, 0 - 720, 1382); boundsInScreen: Rect(0, 72 - 720, 1454); boundsInWindow: Rect(0, 72 - 720, 1454); packageName: id.co.bri.brimo; className: androidx.viewpager.widget.ViewPager; text: null; error: null; maxTextLength: -1; stateDescription: null; contentDescription: null; tooltipText: null; containerTitle: null; viewIdResName: id.co.bri.brimo:id/viewpager; uniqueId: null; checkable: false; checked: false; focusable: true; focused: false; selected: false; clickable: false; longClickable: false; contextClickable: false; enabled: true; password: false; scrollable: false; importantForAccessibility: true; visible: true; actions: [AccessibilityAction: ACTION_FOCUS - null, AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]; isTextSelectable: false
     * 2024-10-20 12:25:59.710 27546-27546 控件信息                    com.example.brimonotification        D  android.view.accessibility.AccessibilityNodeInfo@118e5; boundsInParent: Rect(0, 0 - 720, 1382); boundsInScreen: Rect(0, 72 - 720, 1454); boundsInWindow: Rect(0, 72 - 720, 1454); packageName: id.co.bri.brimo; className: android.view.ViewGroup; text: null; error: null; maxTextLength: -1; stateDescription: null; contentDescription: null; tooltipText: null; containerTitle: null; viewIdResName: null; uniqueId: null; checkable: false; checked: false; focusable: false; focused: false; selected: false; clickable: false; longClickable: false; contextClickable: false; enabled: true; password: false; scrollable: false; importantForAccessibility: false; visible: true; actions: [AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]; isTextSelectable: false
     * 2024-10-20 12:25:59.711 27546-27546 控件信息                    com.example.brimonotification        D  android.view.accessibility.AccessibilityNodeInfo@d914; boundsInParent: Rect(0, 0 - 720, 862); boundsInScreen: Rect(0, 72 - 720, 934); boundsInWindow: Rect(0, 72 - 720, 934); packageName: id.co.bri.brimo; className: androidx.appcompat.widget.LinearLayoutCompat; text: null; error: null; maxTextLength: -1; stateDescription: null; contentDescription: null; tooltipText: null; containerTitle: null; viewIdResName: id.co.bri.brimo:id/ll_1; uniqueId: null; checkable: false; checked: false; focusable: false; focused: false; selected: false; clickable: false; longClickable: false; contextClickable: false; enabled: true; password: false; scrollable: false; importantForAccessibility: false; visible: true; actions: [AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]; isTextSelectable: false
     * 2024-10-20 12:25:59.713 27546-27546 控件信息                    com.example.brimonotification        D  android.view.accessibility.AccessibilityNodeInfo@10620; boundsInParent: Rect(0, 0 - 720, 134); boundsInScreen: Rect(0, 1478 - 720, 1612); boundsInWindow: Rect(0, 1478 - 720, 1612); packageName: id.co.bri.brimo; className: androidx.appcompat.widget.LinearLayoutCompat; text: null; error: null; maxTextLength: -1; stateDescription: null; contentDescription: null; tooltipText: null; containerTitle: null; viewIdResName: id.co.bri.brimo:id/ll_2; uniqueId: null; checkable: false; checked: false; focusable: false; focused: false; selected: false; clickable: false; longClickable: false; contextClickable: false; enabled: true; password: false; scrollable: false; importantForAccessibility: false; visible: true; actions: [AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]; isTextSelectable: false
     * 2024-10-20 12:25:59.715 27546-27546 控件信息                    com.example.brimonotification        D  android.view.accessibility.AccessibilityNodeInfo@11524; boundsInParent: Rect(0, 0 - 720, 72); boundsInScreen: Rect(0, 0 - 720, 72); boundsInWindow: Rect(0, 0 - 720, 72); packageName: id.co.bri.brimo; className: android.view.View; text: null; error: null; maxTextLength: -1; stateDescription: null; contentDescription: null; tooltipText: null; containerTitle: null; viewIdResName: android:id/statusBarBackground; uniqueId: null; checkable: false; checked: false; focusable: false; focused: false; selected: false; clickable: false; longClickable: false; contextClickable: false; enabled: true; password: false; scrollable: false; importantForAccessibility: false; visible: false; actions: [AccessibilityAction: ACTION_SELECT - null, AccessibilityAction: ACTION_CLEAR_SELECTION - null, AccessibilityAction: ACTION_ACCESSIBILITY_FOCUS - null, AccessibilityAction: ACTION_SHOW_ON_SCREEN - null]; isTextSelectable: false
     */
    private final String pass = "Coc135689";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) return;
        handleLogin(nodeInfo);
    }


    /**
     * 处理登录
     *
     * @param nodeInfo
     */
    private void handleLogin(AccessibilityNodeInfo nodeInfo) {
        //点击首页登录
        ClickNodeInfo(nodeInfo, "id.co.bri.brimo:id/btn_login");
        if(EditNodeInfo(nodeInfo, "id.co.bri.brimo:id/et_password", pass)) {
            ClickNodeInfo(nodeInfo, "id.co.bri.brimo:id/button_login");
        }
    }

    /**
     * 输入内容
     *
     * @param nodeInfo
     * @param id
     * @param text
     * @return 如果已经输入，则不需要数据
     */
    private boolean EditNodeInfo(AccessibilityNodeInfo nodeInfo, String id, String text) {
        List<AccessibilityNodeInfo> Passwords = nodeInfo.findAccessibilityNodeInfosByViewId(id);
        for (AccessibilityNodeInfo pass : Passwords) {
            if (pass == null) continue;
            if (pass.getText() == null) {
                editText(pass, text);
                continue;
            }
            String string = pass.getText().toString();
            Log.d("控件消息", string);
            if (!string.equals(text)) {
                editText(pass, text);
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
        Log.d("控件消息", msg);
        Bundle arguments = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, msg);
        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
    }

    @Override
    public void onInterrupt() {

    }
}
