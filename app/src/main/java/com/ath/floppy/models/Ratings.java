package com.ath.floppy.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity(tableName = "ratings", indices = {@Index(value = {"game_id"})},
        foreignKeys = {
                @ForeignKey(
                        entity = Result.class,
                        parentColumns = "game_id",
                        childColumns = "game_id")})
public class Ratings {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "ratings_id")
    @SerializedName("id")
    @Expose
    private int id;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;

    @ColumnInfo(name = "count")
    @SerializedName("count")
    @Expose
    private int count;

    @ColumnInfo(name = "percent")
    @SerializedName("percent")
    @Expose
    private float percent;

    @ColumnInfo(name = "game_id")
    private int game_id;

    public Ratings(int id, String title, int count, float percent, int game_id) {
        this.id = id;
        this.title = title;
        this.count = count;
        this.percent = percent;
        this.game_id = game_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
}
