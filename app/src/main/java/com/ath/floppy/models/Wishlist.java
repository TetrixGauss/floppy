package com.ath.floppy.models;

        import androidx.annotation.NonNull;
        import androidx.room.ColumnInfo;
        import androidx.room.Entity;
        import androidx.room.PrimaryKey;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        import java.util.ArrayList;

@Entity(tableName = "wishlist")
public class Wishlist {

    private ArrayList<Result> wish_list;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "wish_list_id")
    @SerializedName("wish_list_id")
    @Expose
    private int wish_list_id;

    public Wishlist(int wish_list_id) {
        this.wish_list_id = wish_list_id;
    }

    public ArrayList<Result> getWish_list() {
        return wish_list;
    }

    public void setWish_list(ArrayList<Result> wish_list) {
        this.wish_list = wish_list;
    }

    public int getWish_list_id() {
        return wish_list_id;
    }

    public void setWish_list_id(int wish_list_id) {
        this.wish_list_id = wish_list_id;
    }
}

