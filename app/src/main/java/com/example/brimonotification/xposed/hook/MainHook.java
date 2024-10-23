package com.example.brimonotification.xposed.hook;

import com.example.brimonotification.xposed.utils.Log;

import java.io.BufferedReader;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals("id.co.bri.brimo")) {
            ApplicationHook.getInstance(loadPackageParam);
            ActivityHook.getInstance(loadPackageParam).startActivity();
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
    }
}
