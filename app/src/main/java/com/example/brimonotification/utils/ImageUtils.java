package com.example.brimonotification.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @Description
 * @Author 不一样的风景
 * @Time 2024/11/2 18:17
 */
public class ImageUtils {
    public static Bitmap imageToBitmap(Image image) {
        int mWidth = image.getWidth();
        int mHeight = image.getHeight();
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * mWidth;

        Bitmap bitmap = Bitmap.createBitmap(mWidth + rowPadding / pixelStride, mHeight, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);

        return bitmap;
    }

    /**
     * @Description 灰白处理
     * @code 1
     * @Author 不一样的风景
     * @Time 2024/11/2 20:36
     */
    public static Bitmap convertToGrayscale(Bitmap src) {
        // 创建灰度图像
        Bitmap grayScaleBitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(grayScaleBitmap);
        Paint paint = new Paint();
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(new ColorMatrix(new float[]{
                0.33f, 0.33f, 0.33f, 0, 0,
                0.33f, 0.33f, 0.33f, 0, 0,
                0.33f, 0.33f, 0.33f, 0, 0,
                0, 0, 0, 1, 0
        }));
        paint.setColorFilter(filter);
        canvas.drawBitmap(src, 0, 0, paint);
        return grayScaleBitmap;
    }

    /**
     * Save Bitmap
     *
     * @param name file name
     * @param bm   picture to save
     */
    public static void saveBitmap(String name, Bitmap bm, Context mContext) {
        Log.d("Save Bitmap", "Ready to save picture");
        //指定我们想要存储文件的地址
        String TargetPath = mContext.getFilesDir() + "/images/";
        Log.d("Save Bitmap", "Save Path=" + TargetPath);
        //判断指定文件夹的路径是否存在
        if (!new File(TargetPath).isDirectory()) {
            Log.d("Save Bitmap", "TargetPath isn't exist");
            new File(TargetPath).mkdirs();
        }
        //如果指定文件夹创建成功，那么我们则需要进行图片存储操作
        File saveFile = new File(TargetPath, name);
        try {
            FileOutputStream saveImgOut = new FileOutputStream(saveFile);
            // compress - 压缩的意思
            bm.compress(Bitmap.CompressFormat.JPEG, 80, saveImgOut);
            //存储完成后需要清除相关的进程
            saveImgOut.flush();
            saveImgOut.close();
            Log.d("Save Bitmap", "The picture is save to your phone!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
