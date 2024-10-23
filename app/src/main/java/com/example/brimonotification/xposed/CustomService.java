package com.example.brimonotification.xposed;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;

import java.lang.reflect.Method;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import android.os.ICustomService;

import com.example.brimonotification.xposed.utils.Log;

public class CustomService extends ICustomService.Stub {
    private static final String SERVICE_NAME = "custom.service";
    private static Context mContext;
    private static ICustomService mClient;
    private static CustomService mCustomService;

    public CustomService(Context context) {
        mContext = context;
    }

    public static ICustomService getClient() {

        if (mClient == null) {
            try {
                Class<?> ServiceManager = Class.forName("android.os.ServiceManager");
                Method getService = ServiceManager.getDeclaredMethod("getService", String.class);
                mClient = ICustomService.Stub.asInterface((IBinder) getService.invoke(null, getServiceName()));
            } catch (Throwable t) {
                mClient = null;
            }
        }

        return mClient;
    }

    public static String getServiceName() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? "user." + SERVICE_NAME : SERVICE_NAME;
    }

    public static void register(final ClassLoader classLoader) {
        Class<?> ActivityManagerService = XposedHelpers.findClass("com.android.server.am.ActivityManagerService", classLoader);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            XposedBridge.hookAllConstructors(ActivityManagerService, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    register(classLoader, (Context) XposedHelpers.getObjectField(param.thisObject, "mContext"));
                }
            });
        } else {
            XposedBridge.hookAllMethods(ActivityManagerService, "main", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    register(classLoader, (Context) param.getResult());
                }
            });
        }

        XposedBridge.hookAllMethods(ActivityManagerService, "systemReady", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                mCustomService.systemReady();
            }
        });
    }

    private static void register(final ClassLoader classLoader, Context context) {
        mCustomService = new CustomService(context);
        Class<?> ServiceManager = XposedHelpers.findClass("android.os.ServiceManager", classLoader);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            XposedHelpers.callStaticMethod(ServiceManager, "addService", getServiceName(), mCustomService, true);
        } else {
            XposedHelpers.callStaticMethod(ServiceManager, "addService", getServiceName(), mCustomService);
        }
    }

    private void systemReady() {

        Log.print("服务启动");
        // Make initialization here
    }
}