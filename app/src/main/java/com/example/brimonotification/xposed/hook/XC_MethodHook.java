package com.example.brimonotification.xposed.hook;

public class XC_MethodHook extends de.robv.android.xposed.XC_MethodHook {
    private final OnHookedMethod afterHookedMethod;

    public XC_MethodHook(OnHookedMethod afterHookedMethod) {
        this.afterHookedMethod = afterHookedMethod;
    }

    public XC_MethodHook() {
        this.afterHookedMethod = null;
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        if (afterHookedMethod != null) {
            afterHookedMethod.HookedMethod(1, param);
        }
    }

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        if (afterHookedMethod != null) {
            afterHookedMethod.HookedMethod(0, param);
        }
    }

    public interface OnHookedMethod {
        void HookedMethod(int state, MethodHookParam param) throws Throwable;

    }
}
