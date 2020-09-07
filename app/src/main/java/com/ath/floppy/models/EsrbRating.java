package com.ath.floppy.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EsrbRating {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("name")
    @Expose
    private String name;

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
}
