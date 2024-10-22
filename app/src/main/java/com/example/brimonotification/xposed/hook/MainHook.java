package com.example.brimonotification.xposed.hook;

import com.example.brimonotification.xposed.utils.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals("id.co.bri.brimo")) {
            Log.print("运行");
            ApplicationHook.getInstance(loadPackageParam);
            ActivityHook.getInstance(loadPackageParam).startActivity();
            XposedHelpers.findAndHookMethod(StackTraceElement.class, "getClassName", new XC_MethodHook() {

                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    String className = (String) param.getResult();
                    if (className != null && className.contains("xposed")) {
                        Log.print("检测到：" + className);
                        param.setResult("android.os.Handler");
                    }
                    super.afterHookedMethod(param);
                }
            });
        }
    }
}
