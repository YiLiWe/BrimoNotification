package com.example.brimonotification.xposed.hook;

import com.example.brimonotification.xposed.utils.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals("id.co.bri.brimo")) {
            Log.print("运行");
            ApplicationHook.getInstance(loadPackageParam);
            ActivityHook.getInstance(loadPackageParam).startActivity();
        }
    }
}
