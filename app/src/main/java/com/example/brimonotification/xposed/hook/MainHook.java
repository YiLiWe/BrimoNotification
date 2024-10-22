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
            XposedHelpers.findAndHookMethod(StackTraceElement.class, "getClassName", new XC_MethodHook() {

                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    String className = (String) param.getResult();
                    if (className != null && className.contains("brimonotification")) {
                        Log.print("检测到：" + className);
                        param.setResult("android.os.Handler");
                    }
                    super.afterHookedMethod(param);
                }
            });

            XposedHelpers.findAndHookMethod(ClassLoader.class, "loadClass", String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if (param.args != null && param.args[0] != null && param.args[0].toString().contains("brimonotification")) {
                        Log.print("检测到:" + param.args[0]);
                        // 改成一个不存在的类
                        param.args[0] = "android.os.Handler";
                    }

                    super.beforeHookedMethod(param);
                }
            });
            ApplicationHook.getInstance(loadPackageParam);
            ActivityHook.getInstance(loadPackageParam).startActivity();
        }
    }
}
