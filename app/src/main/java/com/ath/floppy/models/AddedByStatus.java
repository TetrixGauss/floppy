package com.ath.floppy.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddedByStatus {

    @SerializedName("yet")
    @Expose
    private int yet;
    @SerializedName("owned")
    @Expose
    private int owned;
    @SerializedName("beaten")
    @Expose
    private int beaten;
    @SerializedName("toplay")
    @Expose
    private int toplay;
    @SerializedName("dropped")
    @Expose
    private int dropped;
    @SerializedName("playing")
    @Expose
    private int playing;

//    public AddedByStatus(int yet, int owned, int beaten, int toplay, int dropped, int playing) {
//        this.yet = yet;
//        this.owned = owned;
//        this.beaten = beaten;
//        this.toplay = toplay;
//        this.dropped = dropped;
//        this.playing = playing;
//    }

    public int getYet() {
        return yet;
    }

    public void setYet(int yet) {
        this.yet = yet;
    }

    public int getOwned() {
        return owned;
    }

    public void setOwned(int owned) {
        this.owned = owned;
    }

    public int getBeaten() {
        return beaten;
    }

    public void setBeaten(int beaten) {
        this.beaten = beaten;
    }

    public int getToplay() {
        return toplay;
    }

    public void setToplay(int toplay) {
        this.toplay = toplay;
    }

    public int getDropped() {
        return dropped;
    }

    public void setDropped(int dropped) {
        this.dropped = dropped;
    }

    public int getPlaying() {
        return playing;
    }

    public void setPlaying(int playing) {
        this.playing = playing;
    }
}
