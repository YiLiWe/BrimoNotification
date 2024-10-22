package com.example.brimonotification.xposed.hook;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.brimonotification.xposed.utils.Log;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import lombok.Data;

//hook界面相关
@Data
public class ActivityHook {
    private static ActivityHook activityHook;
    private final XC_LoadPackage.LoadPackageParam loadPackageParam;

    public void startSetContentView() {
        XposedBridge.hookAllMethods(Activity.class, "setContentView", new XC_MethodHook(this::XC_setContentView));
    }

    private void XC_setContentView(int i, de.robv.android.xposed.XC_MethodHook.MethodHookParam param) {
        if (i == 0) return;//获取控件
        if (param.args[0] instanceof View view) {

        }
    }

    /**
     * 抓取跳转信息
     */
    public void startActivity() {
        XposedBridge.hookAllMethods(Activity.class,"finish",new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (param.thisObject instanceof Activity activity){
                    param.setResult(null); // 阻止原方法执行
                }
                Log.print("关闭");
            }
        });
        XposedBridge.hookAllMethods(Context.class,"startActivity",new XC_MethodHook(this::startActivity));
    }

    private void startActivity(int i, de.robv.android.xposed.XC_MethodHook.MethodHookParam param) {
        if (i == 0 && param.args[0] instanceof Intent intent) {
            handleIntent(intent);
        }
    }

    private void handleIntent(Intent data) {
        Bundle extras = data.getExtras(); // 获取附加数据
        ComponentName name = data.getComponent();
        if (name != null) {
            Log.print("界面:" + name);
        }
        if (extras == null) return;
        for (String key : extras.keySet()) {
            Log.print("Key: " + key + "|Value: " + extras.get(key));
        }
    }

    public static ActivityHook getInstance(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (activityHook == null) {
            activityHook = new ActivityHook(loadPackageParam);
        }
        return activityHook;
    }

}
