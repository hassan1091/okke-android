package com.twittzel.Hassan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twittzel.Hassan.R;
import com.twittzel.Hassan.data.api.result.Datum;

import java.util.List;

public class DownLoadTwitterAdapter extends RecyclerView.Adapter<DownLoadTwitterAdapter.ViewHolderDownLoad> {
    public interface OnDownLoadTwitterItemClickListener {
        void onItemDownLoadTwitterClicked(Datum datum);
    }

    private List<Datum> datumList;
    private OnDownLoadTwitterItemClickListener onDownLoadTwitterItemClickListener;

    public DownLoadTwitterAdapter(List<Datum> datumList, OnDownLoadTwitterItemClickListener onDownLoadTwitterItemClickListener) {
        this.datumList = datumList;
        this.onDownLoadTwitterItemClickListener = onDownLoadTwitterItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolderDownLoad onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_download, parent, false);
        return new ViewHolderDownLoad(view, onDownLoadTwitterItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDownLoad holder, int position) {
        holder.bind(datumList.get(position));
    }


    @Override
    public int getItemCount() {
        return datumList.size();
    }


    static class ViewHolderDownLoad extends RecyclerView.ViewHolder {
        private Datum datum;
        private TextView mSizeTextView;
        private TextView mQualityTextView;

        private ViewHolderDownLoad(@NonNull View itemView, OnDownLoadTwitterItemClickListener onDownLoadTwitterItemClickListener) {
            super(itemView);

            getViews();

            getListener(onDownLoadTwitterItemClickListener);
        }

        private void getViews() {
            mSizeTextView = itemView.findViewById(R.id.size_text_view);
            mQualityTextView = itemView.findViewById(R.id.quality_text_view);
        }

        private void getListener(final OnDownLoadTwitterItemClickListener onDownLoadTwitterItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDownLoadTwitterItemClickListener.onItemDownLoadTwitterClicked(datum);
                }
            });
        }

        private void bind(final Datum datum) {
            this.datum = datum;
            mSizeTextView.setText(datum.getSzie());
            mQualityTextView.setText(datum.getQuality());
        }

    }
}
