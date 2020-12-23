package com.twittzel.Hassan.data.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Url")
public class LastUrlList {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "urls")
    public String lastDownLoadUrl;

    public String filePath;

    public String getLastDownLoadUrl() {
        return lastDownLoadUrl;
    }

    public void setLastDownLoadUrl(String lastDownLoadUrl) {
        this.lastDownLoadUrl = lastDownLoadUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
