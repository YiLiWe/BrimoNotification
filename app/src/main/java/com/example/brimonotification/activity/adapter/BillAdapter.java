package com.example.brimonotification.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brimonotification.activity.BillActivity;
import com.example.brimonotification.bean.NotificationBean;
import com.example.brimonotification.databinding.ItemBillBinding;
import com.example.brimonotification.room.entity.BillEntity;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
    private final List<BillEntity> entities;

    public BillAdapter(List<BillEntity> entities) {
        this.entities = entities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemBillBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemBillBinding binding = ItemBillBinding.bind(holder.itemView);
        BillEntity logEntity = entities.get(position);
        binding.name.setText(logEntity.getPayerName());
        binding.time.setText(logEntity.getTime());
        binding.money.setText(String.valueOf(logEntity.getAmount()));
        switch (logEntity.getState()) {
            case 0 -> binding.state.setText("提交失败");
            case 1 -> binding.state.setText("提交成功");
            case 2 -> binding.state.setText("处理中");
        }
    }


    @Override
    public int getItemCount() {
        return entities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
