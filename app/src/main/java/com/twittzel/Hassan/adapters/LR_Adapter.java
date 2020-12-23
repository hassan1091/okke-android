package com.twittzel.Hassan.adapters;


import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.twittzel.Hassan.R;
import com.twittzel.Hassan.data.database.LastUrlList;

import java.io.File;
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
        return new LR_viewHolder(view, onItemClickListener, lastUserUrlArray);
    }

    @Override
    public void onBindViewHolder(@NonNull LR_viewHolder holder, int position) {
        holder.bind(lastUserUrlArray.get(position));
    }

    @Override
    public int getItemCount() {
        return lastUserUrlArray.size();
    }

    static class LR_viewHolder extends RecyclerView.ViewHolder {
        Uri uri;
        private TextView textView;
        private ImageView shareImageView;
        private VideoView videoView;
        private ConstraintLayout constraintLayout;

        public LR_viewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener, final List<LastUrlList> lastUserUrlArray) {
            super(itemView);

            textView = itemView.findViewById(R.id.BottomCapy);
            shareImageView = itemView.findViewById(R.id.image_view_share);
            videoView = itemView.findViewById(R.id.videoView);
            constraintLayout =itemView.findViewById(R.id.constraint_layout);
          /*  itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickListener(getAdapterPosition(), lastUserUrlArray);
                }
            });*/
        }

        public void bind(LastUrlList lastUrlList) {
            File f = new File(lastUrlList.getFilePath());
            if (!f.exists()){
                constraintLayout.setVisibility(View.GONE);
                return;
            }
            constraintLayout.setVisibility(View.VISIBLE);
            uri = Uri.parse(lastUrlList.getFilePath());
            textView.setText(lastUrlList.getLastDownLoadUrl());

            videoView.setVideoURI(uri);
            videoView.setMediaController(new MediaController(itemView.getContext()));


            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareVideo();
                }
            });
        }

        private void shareVideo() {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("video/*");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, itemView.getContext().getString(R.string.app_name));
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            itemView.getContext().startActivity(Intent.createChooser(sharingIntent, "Share Video"));
        }
    }
}
