package com.example.brimonotification.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.example.brimonotification.R;
import com.example.brimonotification.databinding.ActivityMainBinding;
import com.example.brimonotification.service.NotifyService;
import com.example.brimonotification.service.TaskAccessibilityService;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initClick();
        initToolbar();
    }

    private void initToolbar() {
        try {
            PackageInfo info = getPackageManager().
                    getPackageInfo(getPackageName(), 0);
            binding.toolbar.setTitle(String.format("%s v%s", getString(R.string.app_name), info.versionCode));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean OnMenu(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.bill) {
            startActivity(new Intent(this, BillActivity.class));
        }
        return false;
    }


    private void initClick() {
        binding.notify.setOnClickListener(this::ClickNotify);
        binding.toolbar.setOnMenuItemClickListener(this::OnMenu);
        binding.notionalPooling.setOnClickListener(this::ClickNotionalPooling);
        binding.system.setOnClickListener(this::CLickSystem);
    }


    private void CLickSystem(View view) {
        startService(new Intent(this, TaskAccessibilityService.class));
        Toast.makeText(this, "开启成功", Toast.LENGTH_SHORT).show();
    }

    // .\adb shell pm grant com.example.brimonotification android.permission.WRITE_SECURE_SETTINGS
    private void ClickNotionalPooling(View view) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

    private void ClickNotify(View view) {
        if (!isNLServiceEnabled()) {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        } else if (!Settings.canDrawOverlays(this)) {
            requestOverlayPermission();
        } else {
            Toast.makeText(this, "开启成功", Toast.LENGTH_SHORT).show();
            NotifyService.toggleNotificationListenerService(this);
            Intent service = new Intent(this, NotifyService.class);
            startService(service);
        }
    }

    private void requestOverlayPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }


    /**
     * 是否启用通知监听服务
     *
     * @return
     */
    public boolean isNLServiceEnabled() {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(this);
        return packageNames.contains(getPackageName());
    }

}
