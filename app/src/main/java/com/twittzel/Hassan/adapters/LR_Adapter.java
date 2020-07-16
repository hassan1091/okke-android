package com.twittzel.Hassan.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twittzel.Hassan.R;
import com.twittzel.Hassan.data.database.LastUrlList;

import java.util.List;

public class LR_Adapter extends RecyclerView.Adapter<LR_Adapter.LR_viewHolder> {
    public interface OnItemClickListener {
        void onItemClickListener(int position, List<LastUrlList> lastUserUrlArray);
    }
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
        return new LR_viewHolder(view, onItemClickListener,lastUserUrlArray);
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
                    onItemClickListener.onItemClickListener(getAdapterPosition(),lastUserUrlArray);
                }
            });

        }
    }
}
