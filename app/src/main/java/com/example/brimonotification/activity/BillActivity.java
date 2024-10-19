package com.example.brimonotification.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.brimonotification.R;
import com.example.brimonotification.activity.adapter.BillAdapter;
import com.example.brimonotification.bean.NotificationBean;
import com.example.brimonotification.databinding.ActivityBillBinding;
import com.example.brimonotification.helper.MyDBOpenHelper;
import com.example.brimonotification.runnable.PostDataRunnable;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class BillActivity extends AppCompatActivity  {
    private ActivityBillBinding binding;
    private int pageSize = 10, pageNumber = 1;
    private final List<NotificationBean> beans = new ArrayList<>();
    private BillAdapter adapter;
    private MyDBOpenHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        helper = new MyDBOpenHelper(this);
        initRecycler();
        initToolbar();
        initSmart();
    }

    private void initRecycler() {
        adapter = new BillAdapter(beans, this);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(adapter);
        binding.recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void initSmart() {
        binding.smart.setOnRefreshListener(this::onRefresh);
        binding.smart.setOnLoadMoreListener(this::onLoadMore);
    }

    private void onLoadMore(RefreshLayout refreshLayout) {
        pageNumber = pageNumber + 1;
        initData();
    }

    private void initData() {
        binding.smart.finishLoadMore(0);
        binding.smart.finishRefresh(0);

        int offset = (pageNumber - 1) * pageSize;
        JSONArray data = helper.getResults("SELECT * FROM notification  ORDER BY id DESC LIMIT ? OFFSET ?", new String[]{String.valueOf(pageSize), String.valueOf(offset)});
        for (int i = 0; i < data.size(); i++) {
            JSONObject object = data.getJSONObject(i);
            beans.add(object.to(NotificationBean.class));
            adapter.notifyItemInserted(beans.size() - 1);
        }
    }

    private void onRefresh(RefreshLayout refreshLayout) {
        adapter.notifyItemRangeRemoved(0, beans.size() + 1);
        beans.clear();
        pageSize = 10;
        pageNumber = 1;
        initData();
    }

    /**
     * 适配器调用该方法提交数据
     * @param bean
     */
    public void post(NotificationBean bean) {
        PostDataRunnable postDataRunnable = new PostDataRunnable(bean, bean.getId());
        postDataRunnable.setOnMessage(this::onMessage);
    }


    private void onMessage(String msg, long id) {
        runOnUiThread(() -> Toast.makeText(BillActivity.this, msg, Toast.LENGTH_SHORT).show());
    }


    /**
     * 适配器调用删除数据
     * @param id
     */
    public void delete(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("notification", "id = ?", new String[]{String.valueOf(id)});
    }

    private void initToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.toolbar.setOnMenuItemClickListener(this::OnMenu);
    }

    private boolean OnMenu(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.delete) {
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("DELETE FROM notification"); // 假设你的表名为 log
            adapter.notifyItemRangeRemoved(0, beans.size() + 1);
            beans.clear();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
    }

}
