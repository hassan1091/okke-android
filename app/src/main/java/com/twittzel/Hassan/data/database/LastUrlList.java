package com.twittzel.Hassan.data.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;


@Entity(tableName = "Url")
public class LastUrlList {
    @ColumnInfo(name = "urls")
    @PrimaryKey()
    @NonNull
    public String lastDownLoadUrl = "";

    public String filePath;

    public LastUrlList() {
    }

    @NotNull
    public String getLastDownLoadUrl() {
        return lastDownLoadUrl;
    }

    public void setLastDownLoadUrl(@NotNull String lastDownLoadUrl) {
        this.lastDownLoadUrl = lastDownLoadUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
