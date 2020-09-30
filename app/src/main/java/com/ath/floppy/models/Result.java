package com.ath.floppy.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity(tableName = "games")
public class Result implements Parcelable {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "game_id")
    @SerializedName("id")
    @Expose
    private int id;

    @ColumnInfo(name = "favorite")
    private boolean favorite;

    @ColumnInfo(name = "wish")
    private boolean wish;

    @ColumnInfo(name = "slug")
    @SerializedName("slug")
    @Expose
    private String slug;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name;

    @ColumnInfo(name = "released")
    @SerializedName("released")
    @Expose
    private String released;

    @ColumnInfo(name = "tba")
    @SerializedName("tba")
    @Expose
    private boolean tba;

    @ColumnInfo(name = "background_image")
    @SerializedName("background_image")
    @Expose
    private String backgroundImage;

    @ColumnInfo(name = "rating")
    @SerializedName("rating")
    @Expose
    private double rating;

    @ColumnInfo(name = "rating_top")
    @SerializedName("rating_top")
    @Expose
    private int ratingTop;

    @Ignore
    @SerializedName("ratings")
    @Expose
    private ArrayList<Ratings> ratings;

    @ColumnInfo(name = "ratings_count")
    @SerializedName("ratings_count")
    @Expose
    private int ratingsCount;

    @ColumnInfo(name = "reviews_text_count")
    @SerializedName("reviews_text_count")
    @Expose
    private String reviewsTextCount;

    @ColumnInfo(name = "added")
    @SerializedName("added")
    @Expose
    private int added;

    @Ignore
    @SerializedName("added_by_status")
    @Expose
    private AddedByStatus addedByStatus;

    @ColumnInfo(name = "metacritic")
    @SerializedName("metacritic")
    @Expose
    private int metacritic;

    @ColumnInfo(name = "playtime")
    @SerializedName("playtime")
    @Expose
    private int playtime;

    @ColumnInfo(name = "suggestions_count")
    @SerializedName("suggestions_count")
    @Expose
    private int suggestionsCount;

    @Ignore
    @SerializedName("esrb_rating")
    @Expose
    private EsrbRating esrbRating;

    @Ignore
    @SerializedName("platforms")
    @Expose
    private ArrayList<Platform> platforms;

    public Result(int id, String slug, String name, String released, boolean tba,
                  String backgroundImage, double rating, int ratingTop,
                  int ratingsCount, String reviewsTextCount, int added,
                  int metacritic, int playtime, int suggestionsCount, boolean favorite, boolean wish) {
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.released = released;
        this.tba = tba;
        this.backgroundImage = backgroundImage;
        this.rating = rating;
        this.ratingTop = ratingTop;

        this.ratingsCount = ratingsCount;
        this.reviewsTextCount = reviewsTextCount;
        this.added = added;

        this.metacritic = metacritic;
        this.playtime = playtime;
        this.suggestionsCount = suggestionsCount;

        this.favorite = favorite;
        this.wish = wish;


    }

    @Ignore
    public Result() {
    }

    protected Result(Parcel in) {
        id = in.readInt();
        slug = in.readString();
        name = in.readString();
        released = in.readString();
        tba = in.readByte() != 0;
        backgroundImage = in.readString();
        rating = in.readDouble();
        ratingTop = in.readInt();
        ratingsCount = in.readInt();
        reviewsTextCount = in.readString();
        added = in.readInt();
        metacritic = in.readInt();
        playtime = in.readInt();
        suggestionsCount = in.readInt();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isWish() {
        return wish;
    }

    public void setWish(boolean wish) {
        this.wish = wish;
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

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public boolean isTba() {
        return tba;
    }

    public void setTba(boolean tba) {
        this.tba = tba;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingTop() {
        return ratingTop;
    }

    public void setRatingTop(int ratingTop) {
        this.ratingTop = ratingTop;
    }

    public ArrayList<Ratings> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<Ratings> ratings) {
        this.ratings = ratings;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public String getReviewsTextCount() {
        return reviewsTextCount;
    }

    public void setReviewsTextCount(String reviewsTextCount) {
        this.reviewsTextCount = reviewsTextCount;
    }

    public int getAdded() {
        return added;
    }

    public void setAdded(int added) {
        this.added = added;
    }

    public AddedByStatus getAddedByStatus() {
        return addedByStatus;
    }

    public void setAddedByStatus(AddedByStatus addedByStatus) {
        this.addedByStatus = addedByStatus;
    }

    public int getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(int metacritic) {
        this.metacritic = metacritic;
    }

    public int getPlaytime() {
        return playtime;
    }

    public void setPlaytime(int playtime) {
        this.playtime = playtime;
    }

    public int getSuggestionsCount() {
        return suggestionsCount;
    }

    public void setSuggestionsCount(int suggestionsCount) {
        this.suggestionsCount = suggestionsCount;
    }

    public EsrbRating getEsrbRating() {
        return esrbRating;
    }

    public void setEsrbRating(EsrbRating esrbRating) {
        this.esrbRating = esrbRating;
    }

    public ArrayList<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(ArrayList<Platform> platforms) {
        this.platforms.addAll(platforms);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(slug);
        dest.writeString(name);
        dest.writeString(released);
        dest.writeByte((byte) (tba ? 1 : 0));
        dest.writeString(backgroundImage);
        dest.writeDouble(rating);
        dest.writeInt(ratingTop);
        dest.writeInt(ratingsCount);
        dest.writeString(reviewsTextCount);
        dest.writeInt(added);
        dest.writeInt(metacritic);
        dest.writeInt(playtime);
        dest.writeInt(suggestionsCount);
    }
}
