package com.ath.floppy.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ath.floppy.models.Platform;
import com.ath.floppy.models.Ratings;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface RatingsDAO {

    @Query("SELECT * FROM ratings")
    LiveData<List<Ratings>> getAllRatings();

    @Insert(onConflict = REPLACE)
    void insert(Ratings ratings);

    @Delete
    void delete(Ratings ratings);

    @Update(onConflict = REPLACE)
    void update(Ratings ratings);
}
