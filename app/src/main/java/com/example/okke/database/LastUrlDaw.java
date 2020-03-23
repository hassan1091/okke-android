package com.example.okke.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LastUrlDaw {

    @Insert
    void InsertUrl(LastUrlList lastUrlList);

    @Query("SELECT * FROM Url")
    List<LastUrlList> getLastUrlList();

}
