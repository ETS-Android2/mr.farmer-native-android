package com.snag.ink.user.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MainDao {

    //Insert query
    @Insert(onConflict = REPLACE)
    void insert(MainData mainData);

    //Delete Query
    @Delete
    void delete(MainData mainData);

    //Delete all
    @Delete
    void reset(List<MainData> mainData);

    //update query
    @Query("UPDATE table_name SET itemname = :sText WHERE ID = :sID")
    void update(int sID, String sText);

    //Get all data
    @Query("SELECT * FROM table_name")
    List<MainData> getall();

    //Get sum of prices
    @Query("SELECT SUM(itemprice*itemcount) FROM table_name")
    int gettotalcost();

}
