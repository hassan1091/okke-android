package com.twittzel.Hassan.listener;

import com.twittzel.Hassan.data.database.LastUrlList;

import java.util.List;

public interface OnItemClickListener {
    void onItemClickListener(int position, List<LastUrlList> lastUserUrlArray);
}
