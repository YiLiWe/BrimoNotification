package com.example.brimonotification.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Description 文件工具类
 * @Author 不一样的风景
 * @Time 2024/11/2 19:24
 */
public class FileUtils {
    /**
     * @Description Assets文件复制到私有文件夹
     * @code 1
     * @Author 不一样的风景
     * @Time 2024/11/2 19:27
     */
    public static void copyAssetToPrivateFolder(Context context, String assetFileName) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            // 从 assets 读取文件
            inputStream = context.getAssets().open(assetFileName);

            // 在私有文件夹中创建文件
            File file = new File(context.getFilesDir(), "tessdata");
            if (!file.isDirectory()) {
                file.mkdirs();
            }
            File outFile = new File(file, assetFileName);
            outputStream = new FileOutputStream(outFile);

            // 进行复制
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }

            outputStream.flush();
            // 复制成功
            Log.d("FileUtils", "文件复制成功: " + outFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FileUtils", "文件复制失败: " + e.getMessage());
        } finally {
            // 关闭流
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
