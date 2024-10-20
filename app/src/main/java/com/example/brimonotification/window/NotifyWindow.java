package com.example.brimonotification.window;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.brimonotification.databinding.LayoutWindowBinding;
import com.example.brimonotification.service.NotifyService;

import lombok.Data;

@Data
public class NotifyWindow {
    private int xOffset, yOffset;
    private float x, y;
    private boolean isDragging = false;
    private static NotifyWindow notifyWindow;
    private final WindowManager windowManager;
    private LayoutWindowBinding binding;
    private final NotifyService service;
    private final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // 对于 Android 8.0 及以上
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);
    private int LineCount;
    private int MaxLineCount = 100;

    public NotifyWindow(NotifyService service) {
        this.service = service;
        binding = LayoutWindowBinding.inflate(LayoutInflater.from(service));
        windowManager = (WindowManager) service.getSystemService(Context.WINDOW_SERVICE);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void start() {
        windowManager.addView(binding.getRoot(), params);
        binding.image.setOnTouchListener(this::onTouch);
        binding.close.setOnClickListener(this::ClickCLose);
    }

    public void appText(String msg) {
        if (MaxLineCount <= LineCount) {
            LineCount = 0;
            binding.text.setText("");
        }
        binding.text.append(msg);
        binding.nested.post(() -> binding.nested.fullScroll(View.FOCUS_DOWN));
        LineCount++;
    }

    private void ClickCLose(View view) {
        binding.l.setVisibility(View.GONE);
        binding.image.setVisibility(View.VISIBLE);
    }

    public void onDestroy() {
        windowManager.removeView(binding.getRoot());
    }

    private boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getRawX();
                y = event.getRawY();
                xOffset = (int) (x - params.x);
                yOffset = (int) (y - params.y);
                isDragging = false;
                return true;

            case MotionEvent.ACTION_MOVE:
                isDragging = true;
                params.x = (int) (event.getRawX() - xOffset);
                params.y = (int) (event.getRawY() - yOffset);
                windowManager.updateViewLayout(binding.getRoot(), params);
                return true;

            case MotionEvent.ACTION_UP:
                if (!isDragging) {
                    boolean is = binding.l.getVisibility() == View.VISIBLE;
                    int visibility = is ? View.GONE : View.VISIBLE;
                    binding.l.setVisibility(visibility);
                    binding.image.setVisibility(View.GONE);
                }
                return true;

            default:
                return false;
        }
    }

    public static NotifyWindow getNotifyWindow(NotifyService notifyService) {
        if (notifyWindow == null) {
            notifyWindow = new NotifyWindow(notifyService);
        }
        return notifyWindow;
    }
}
