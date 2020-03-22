package com.example.okke.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Url")
public class LastUrlList {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "urls")
    public String mLRDownList;


    public String getmLRDownList() {
        return mLRDownList;
    }

    public void setmLRDownList(String mLRDownList) {
        this.mLRDownList = mLRDownList;
    }
}
