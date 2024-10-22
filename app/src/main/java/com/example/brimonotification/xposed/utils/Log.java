package com.example.brimonotification.xposed.utils;

public class Log {
    private static final String tag = "模块";

    public static void print(String msg) {
        Log(tag, msg);
    }

    private static void Log(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            android.util.Log.d(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        android.util.Log.i(tag, msg);
    }

}
