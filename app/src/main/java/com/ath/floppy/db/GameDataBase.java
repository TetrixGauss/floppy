package com.ath.floppy.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ath.floppy.models.Platform;
import com.ath.floppy.models.Ratings;
import com.ath.floppy.models.Result;


@Database(entities = {Result.class, Platform.class, Ratings.class}, version = 5, exportSchema = false)
public abstract class GameDataBase extends RoomDatabase {
    public abstract ResultDAO resultDAO();
    public abstract PlatformDAO platformDAO();
    public abstract RatingsDAO ratingsDAO();
//    public abstract RequirementsDAO requirementsDAO();

    private static GameDataBase INSTANCE;

    public static GameDataBase getInstance(final Context context) {
        if (INSTANCE == null){
            synchronized (GameDataBase.class) {
                if (INSTANCE == null){
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GameDataBase.class, "game_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
