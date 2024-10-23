package com.example.brimonotification.xposed.hook;

import com.github.kyuubiran.ezxhelper.init.EzXHelperInit;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals("id.co.bri.brimo")) {
            EzXHelperInit.INSTANCE.initHandleLoadPackage(loadPackageParam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        EzXHelperInit.INSTANCE.initZygote(startupParam);
    }
}
