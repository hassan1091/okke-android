package com.twittzel.hassan.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LastUrlDaw {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertUrl(LastUrlList lastUrlList);

    @Query("SELECT * FROM Url")
    List<LastUrlList> getLastUrlList();

}
