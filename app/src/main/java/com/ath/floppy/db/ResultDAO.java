package com.ath.floppy.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ath.floppy.models.Platform;
import com.ath.floppy.models.Ratings;
import com.ath.floppy.models.Result;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ResultDAO {

    @Query("SELECT * FROM games")
    LiveData<List<Result>> getAllResults();

    @Query("SELECT * FROM platforms")
    LiveData<List<Platform>> getAllPlatforms();
    //
    //    @Query("SELECT * FROM ratings")
    //    LiveData<List<Ratings>> getAllRatings();
    //
    //    @Query("SELECT * FROM req")
    //    LiveData<List<Platform>> getAllPlatforms();

    @Query("SELECT * FROM games, platforms WHERE (platforms.platform_id = :id AND platforms.game_id = :gameId)")
    LiveData<List<Result>> getResultWithPlarformId(int id, int gameId);

    @Query("SELECT * FROM games WHERE games.game_id = :id")
    LiveData<List<Result>> getGamesWithId(int id);

    @Insert(onConflict = REPLACE)
    void insert(Result result);

    @Delete
    void delete(Result result);

    @Update(onConflict = REPLACE)
    void update(Result result);
}
