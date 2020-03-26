package com.twittzel.hassan.listener;

import com.twittzel.hassan.data.database.LastUrlList;

import java.util.List;

public interface OnItemClickListener {
    void OnItemClickListener(int position, List<LastUrlList> lastUserUrlArray);
}
