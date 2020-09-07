package com.ath.floppy.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ath.floppy.models.Platform;
import com.ath.floppy.models.Result;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface PlatformDAO {

    @Query("SELECT * FROM platforms")
    LiveData<List<Platform>> getAllPlatforms();

    @Insert(onConflict = REPLACE)
    void insert(Platform platform);

    @Delete
    void delete(Platform platform);

    @Update(onConflict = REPLACE)
    void update(Platform platform);

    @Query("SELECT * FROM platforms WHERE platforms.platform_id = :id")
    LiveData<List<Platform>> getPlatformsWithId(int id);
}

