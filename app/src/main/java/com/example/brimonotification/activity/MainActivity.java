package com.example.brimonotification.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.example.brimonotification.R;
import com.example.brimonotification.databinding.ActivityMainBinding;
import com.example.brimonotification.service.NotifyService;
import com.example.brimonotification.utils.FileUtils;
import com.hjq.permissions.XXPermissions;
import com.hjq.permissions.permission.PermissionLists;

import java.io.File;
import java.util.Set;

/**
 * @Description 首页
 * @Author 不一样的风景
 * @Time 2024/11/2 17:11
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initXXPermissions();
        initData();
        initClick();
        initToolbar();
        new Thread(this::initAssets).start();
    }

    private void initXXPermissions() {
        if (XXPermissions.isGrantedPermission(this, PermissionLists.getSystemAlertWindowPermission())) {
        } else {
            XXPermissions.with(MainActivity.this)
                    .permission(PermissionLists.getSystemAlertWindowPermission())
                    .request((grantedList, deniedList) -> {
                        if (!grantedList.isEmpty()) {
                        } else {
                            Toast.makeText(MainActivity.this, "请授予悬浮窗权限", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    /**
     * @Description 初始化数据
     * @code 1
     * @Author 不一样的风景
     * @Time 2024/11/9 20:35
     */
    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String card = sharedPreferences.getString("card", "");
        String url = sharedPreferences.getString("url", "");
        binding.card.setText(card);
        binding.url.setText(url);
    }

    private void initAssets() {
        File fileX = new File(getFilesDir(), "tessdata");
        File file = new File(fileX, "chi_sim.traineddata");
        File file1 = new File(fileX, "eng.traineddata");
        if (!file.exists() && !file1.exists()) {
            FileUtils.copyAssetToPrivateFolder(this, "chi_sim.traineddata");
            FileUtils.copyAssetToPrivateFolder(this, "eng.traineddata");
        }
    }

    /**
     * @Description 初始化标题栏
     * @code 1
     * @Author 不一样的风景
     * @Time 2024/11/2 12:23
     */
    private void initToolbar() {
        try {
            PackageInfo info = getPackageManager().
                    getPackageInfo(getPackageName(), 0);
            binding.toolbar.setTitle(String.format("%s v%s", getString(R.string.app_name), info.versionCode));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description 标题栏点击事件
     * @code 1
     * @Author 不一样的风景
     * @Time 2024/11/2 12:22
     */
    private boolean OnMenu(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.bill) {
            startActivity(new Intent(this, BillActivity.class));
        }
        return false;
    }


    /**
     * @Description 初始化控件点击事件
     * @code 1
     * @Author 不一样的风景
     * @Time 2024/11/2 12:21
     */
    private void initClick() {
        binding.notify.setOnClickListener(this::ClickNotify);
        binding.toolbar.setOnMenuItemClickListener(this::OnMenu);
        binding.sava.setOnClickListener(this::savaClick);
    }

    /**
     * @Description 储存卡号
     * @code 1
     * @Author 不一样的风景
     * @Time 2024/11/9 20:32
     */
    private void savaClick(View view) {
        String text = binding.card.getText().toString();
        String url = binding.url.getText().toString();
        if (text.isEmpty() || url.isEmpty()) {
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        getSharedPreferences("DATA", Context.MODE_PRIVATE).edit()
                .putString("card", text)
                .putString("url", url)
                .apply();
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * @Description 点击事件
     * @code 1
     * @Author 不一样的风景
     * @Time 2024/11/2 12:20
     */
    private void ClickNotify(View view) {
        if (!isNLServiceEnabled())//判断是否开启监听服务
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        else if (!Settings.canDrawOverlays(this)) requestOverlayPermission();//判断是否具备悬浮窗权限
        else startNotify();//开启服务
    }

    /**
     * @Description 开启通知栏监听服务
     * @code 1
     * @Author 不一样的风景
     * @Time 2024/11/2 12:15
     */
    private void startNotify() {
        toggleNotificationListenerService(this);
        Intent service = new Intent(this, NotifyService.class);
        startService(service);
        Toast.makeText(this, "开启成功", Toast.LENGTH_SHORT).show();
    }


    /**
     * @param context 反正第二次启动失败
     */
    public static void toggleNotificationListenerService(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context, NotifyService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName(context, NotifyService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    /**
     * @Description 跳转悬浮窗权限界面
     * @code 1
     * @Author 不一样的风景
     * @Time 2024/11/2 12:14
     */
    private void requestOverlayPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    /**
     * @Description 判断是否开启通知栏监听服务
     * @code 1
     * @Author 不一样的风景
     * @Time 2024/11/2 12:09
     */
    public boolean isNLServiceEnabled() {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(this);
        return packageNames.contains(getPackageName());
    }
}
