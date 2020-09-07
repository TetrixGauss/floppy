package com.ath.floppy.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "platforms", indices = {@Index(value = {"game_id"})},
        foreignKeys = {
                @ForeignKey(
                        entity = Result.class,
                        parentColumns = "game_id",
                        childColumns = "game_id")})
public class Platform {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "platform_id")
    @SerializedName("id")
    @Expose
    private int id;

    @ColumnInfo(name = "slug")
    @SerializedName("slug")
    @Expose
    private String slug;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name;

    @ColumnInfo(name = "game_id")
    private int game_id;

    public Platform(int id, String slug, String name, int game_id) {
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.game_id = game_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }
}
