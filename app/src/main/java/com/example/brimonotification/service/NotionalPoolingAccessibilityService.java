package com.example.brimonotification.service;

import android.accessibilityservice.AccessibilityService;
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
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) return;
        List<AccessibilityNodeInfo> nodeInfos = new ArrayList<>();
        AccessibilityUtils.getAccessibilityNodeInfoS(nodeInfos, nodeInfo);
        for (AccessibilityNodeInfo nodeInfo1 : nodeInfos) {
            Log.d("控件信息", nodeInfo1.toString());
        }
        handleLogin(nodeInfo);
    }

    /**
     * 处理登录
     *
     * @param nodeInfo
     */
    private void handleLogin(AccessibilityNodeInfo nodeInfo) {
        List<AccessibilityNodeInfo> logins = nodeInfo.findAccessibilityNodeInfosByViewId("id.co.bri.brimo:id/btn_login");
        for (AccessibilityNodeInfo login : logins) {
            login.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    @Override
    public void onInterrupt() {

    }
}
