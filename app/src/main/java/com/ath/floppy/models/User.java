package com.ath.floppy.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "users")
public class User {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    @Expose
    private int user_id;

    @NonNull
    @ColumnInfo(name = "email")
    @SerializedName("email")
    @Expose
    private String email;

    @NonNull
    @ColumnInfo(name = "userName")
    @SerializedName("userName")
    @Expose
    private String userName;

    @NonNull
    @ColumnInfo(name = "favorite_id")
    @SerializedName("favorite_id")
    @Expose
    private int favorite_id;

    @NonNull
    @ColumnInfo(name = "wish_id")
    @SerializedName("wish_id")
    @Expose
    private int wish_id;

    public User(int user_id, String email, String userName, int favorite_id, int wish_id) {
        this.user_id = user_id;
        this.email = email;
        this.userName = userName;
        this.favorite_id = favorite_id;
        this.wish_id = wish_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getFavorite_id() {
        return favorite_id;
    }

    public void setFavorite_id(int favorite_id) {
        this.favorite_id = favorite_id;
    }

    public int getWish_id() {
        return wish_id;
    }

    public void setWish_id(int wish_id) {
        this.wish_id = wish_id;
    }
}
