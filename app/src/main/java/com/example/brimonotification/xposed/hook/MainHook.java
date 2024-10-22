package com.example.brimonotification.xposed.hook;

import com.example.brimonotification.xposed.utils.Log;

import java.io.BufferedReader;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    private static int modify = 0;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals("id.co.bri.brimo")) {
            Log.print("运行");
            XposedHelpers.findAndHookMethod(StackTraceElement.class, "getClassName", new XC_MethodHook() {

                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    String className = (String) param.getResult();
                    if (className != null && className.contains("brimonotification") || className.contains("xposed")) {
                        Log.print("检测到：" + className);
                        param.setResult("android.os.Handler");
                    }
                    super.afterHookedMethod(param);
                }
            });

            XposedHelpers.findAndHookMethod(ClassLoader.class, "loadClass", String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if (param.args != null && param.args[0] != null && param.args[0].toString().contains("brimonotification") || param.args[0].toString().contains("xposed")) {
                        Log.print("检测到:" + param.args[0]);
                        // 改成一个不存在的类
                        param.args[0] = "android.os.Handler";
                    }

                    super.beforeHookedMethod(param);
                }
            });
            // 定义全局变量 modify
            XposedHelpers.findAndHookMethod(Method.class, "getModifiers", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Method method = (Method) param.thisObject;
                    String[] array = new String[]{"getDeviceId"};
                    String method_name = method.getName();
                    if (Arrays.asList(array).contains(method_name)) {
                        modify = 0;
                    } else {
                        modify = (int) param.getResult();
                    }

                    super.afterHookedMethod(param);
                }
            });

            XposedHelpers.findAndHookMethod(Modifier.class, "isNative", int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    param.args[0] = modify;

                    super.beforeHookedMethod(param);
                }
            });

            XposedHelpers.findAndHookMethod(BufferedReader.class, "readLine", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    String result = (String) param.getResult();
                    if (result != null) {
                        if (result.contains("/data/data/de.robv.android.xposed.installer/bin/XposedBridge.jar")) {
                            param.setResult("");
                            new File("").lastModified();
                        }
                    }

                    super.afterHookedMethod(param);
                }
            });

            XposedHelpers.findAndHookMethod(StackTraceElement.class, "getClassName", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    String result = (String) param.getResult();
                    if (result != null){
                        if (result.contains("xposed")) {
                            param.setResult("");
                            // Log.i(tag, "替换了，字符串名称 " + result);
                        }else if(result.contains("com.android.internal.os.ZygoteInit")){
                            param.setResult("");
                        }
                    }

                    super.afterHookedMethod(param);
                }
            });
            ApplicationHook.getInstance(loadPackageParam);
            ActivityHook.getInstance(loadPackageParam).startActivity();
        }
    }
}
