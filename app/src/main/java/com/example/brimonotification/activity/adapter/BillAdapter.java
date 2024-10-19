package com.example.brimonotification.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brimonotification.activity.BillActivity;
import com.example.brimonotification.bean.NotificationBean;
import com.example.brimonotification.databinding.ItemBillBinding;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
    private final List<NotificationBean> entities;
    private final BillActivity billActivity;

    public BillAdapter(List<NotificationBean> entities, BillActivity billActivity) {
        this.entities = entities;
        this.billActivity = billActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemBillBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemBillBinding binding = ItemBillBinding.bind(holder.itemView);
        NotificationBean logEntity = entities.get(position);
        binding.name.setText(logEntity.getPayerName());
        binding.time.setText(logEntity.getTime());
        binding.money.setText(String.valueOf(logEntity.getAmount()));
        if (logEntity.getState() == 0) {
            binding.post.setOnClickListener(v -> {
                billActivity.post(logEntity);
                binding.state.setText("已提交");
                v.setVisibility(View.GONE);
            });
        } else {
            binding.state.setText("已提交");
        }
        binding.delete.setOnClickListener(v -> {
            int position1 = holder.getLayoutPosition();
            notifyItemRemoved(position1);
            entities.remove(position1);
            billActivity.delete(logEntity.getId());
        });
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
