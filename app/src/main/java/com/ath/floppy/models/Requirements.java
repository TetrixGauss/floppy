package com.ath.floppy.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Requirements {

    @SerializedName("minimum")
    @Expose
    private String minimum;
    @SerializedName("recommended")
    @Expose
    private String recommended;

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getRecommended() {
        return recommended;
    }

    public void setRecommended(String recommended) {
        this.recommended = recommended;
    }

}
