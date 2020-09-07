package com.ath.floppy;

import com.ath.floppy.models.Result;

import java.util.ArrayList;

public class Favorites {

    ArrayList<Result> result;

    public ArrayList<Result> getResult() {
        return result;
    }

    public void setResult(ArrayList<Result> result) {
        this.result = result;
    }

    public Favorites(ArrayList<Result> result) {
        this.result = result;
    }
}
