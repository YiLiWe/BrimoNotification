package com.example.brimonotification.xposed.hook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        XposedHook xposedHook = new XposedHook();
        xposedHook.handleLoadPackage(loadPackageParam);
    }

}
