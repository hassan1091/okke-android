package com.twittzel.hassan.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twittzel.hassan.R;
import com.twittzel.hassan.data.database.LastUrlList;
import com.twittzel.hassan.listener.OnItemClickListener;

import java.util.List;

public class LR_Adapter extends RecyclerView.Adapter<LR_Adapter.LR_viewHolder> {

    List<LastUrlList> lastUserUrlArray;
    OnItemClickListener onItemClickListener;

    public LR_Adapter(List<LastUrlList> lastUserUrlArray, OnItemClickListener onItemClickListener) {
        this.lastUserUrlArray = lastUserUrlArray;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public LR_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_last_down_url, parent, false);
        LR_viewHolder lr_viewHolder = new LR_viewHolder(view, onItemClickListener,lastUserUrlArray);
        return lr_viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LR_viewHolder holder, int position) {
        holder.textView.setText(lastUserUrlArray.get(position).getmLRDownList());
    }

    @Override
    public int getItemCount() {
        return lastUserUrlArray.size();
    }

    static class LR_viewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public LR_viewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener, final List<LastUrlList> lastUserUrlArray) {
            super(itemView);

            textView = itemView.findViewById(R.id.BottomCapy);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.OnItemClickListener(getAdapterPosition(),lastUserUrlArray);
                }
            });

        }
    }
}
