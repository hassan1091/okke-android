package com.example.okke.listener;

import com.example.okke.data.database.LastUrlList;

import java.util.List;

public interface OnItemClickListener {
    void OnItemClickListener(int position, List<LastUrlList> lastUserUrlArray);
}
