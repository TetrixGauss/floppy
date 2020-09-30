package com.ath.floppy.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity(tableName = "favorites")
public class Favorites {

    private ArrayList<Result> favorite_list;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "favorites_list_id")
    @SerializedName("favorites_list_id")
    @Expose
    private int favorites_list_id;

    public Favorites(int favorites_list_id) {
        this.favorites_list_id = favorites_list_id;
    }

    public ArrayList<Result> getFavorite_list() {
        return favorite_list;
    }

    public void setFavorite_list(ArrayList<Result> favorite_list) {
        this.favorite_list = favorite_list;
    }

    public int getFavorites_list_id() {
        return favorites_list_id;
    }

    public void setFavorites_list_id(int favorites_list_id) {
        this.favorites_list_id = favorites_list_id;
    }
}
