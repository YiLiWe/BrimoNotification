package com.example.brimonotification.utils;

import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class AccessibilityUtils {

    public static void getAccessibilityNodeInfoS(List<AccessibilityNodeInfo> nodeInfos, AccessibilityNodeInfo node) {
        if (node == null) {
            return;
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo accessibilityNodeInfo = node.getChild(i);
            if (accessibilityNodeInfo != null) {
                nodeInfos.add(accessibilityNodeInfo);
                getAccessibilityNodeInfoS(nodeInfos, accessibilityNodeInfo);
            }
        }
    }

}
