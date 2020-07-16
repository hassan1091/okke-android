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

public class DownLoadTwitterAdapter extends RecyclerView.Adapter<DownLoadTwitterAdapter.DownLoadHolder> {

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
    public DownLoadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_download, parent, false);
        return new DownLoadHolder(view, onDownLoadTwitterItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DownLoadHolder holder, int position) {
        holder.bind(datumList.get(position));
    }


    @Override
    public int getItemCount() {
        return datumList.size();
    }

    TextView mSizeTextView;
    TextView mQualityTextView;

    public class DownLoadHolder extends RecyclerView.ViewHolder {
        public DownLoadHolder(@NonNull View itemView, OnDownLoadTwitterItemClickListener onDownLoadTwitterItemClickListener) {
            super(itemView);
            mSizeTextView = itemView.findViewById(R.id.size_text_view);
            mQualityTextView = itemView.findViewById(R.id.quality_text_view);
        }

        private void bind(final Datum datum) {
            mSizeTextView.setText(datum.getSzie());
            mQualityTextView.setText(datum.getQuality());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDownLoadTwitterItemClickListener.onItemDownLoadTwitterClicked(datum);
                }
            });
        }
    }
}
