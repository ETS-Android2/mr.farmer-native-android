package com.snag.ink.user.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MainData.class}, version = 2, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {

    //Database instance
    public static RoomDB database;

    //Database name
    private static String DATABASE_NAME = "database";

    public synchronized static RoomDB getInstance(Context context) {

        //check condition
        if (database == null) {
            //when database is null intialize database
            database = Room.databaseBuilder(context.getApplicationContext()
                    , RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
    //create DAO
    public abstract MainDao mainDao();
}













