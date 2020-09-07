package com.ath.floppy.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ath.floppy.models.Result;
import com.ath.floppy.repository.Repo;

import java.util.List;

public class ResultViewModel extends AndroidViewModel {

    private Repo repo;
    private LiveData<List<Result>> results;

    public ResultViewModel(@NonNull Application application) {
        super(application);
        repo = new Repo(application);
        results = repo.getAllResults();
    }

    public LiveData<List<Result>> getResults() {
        return results;
    }

    public void setResults(LiveData<List<Result>> results) {
        this.results = results;
    }

    public void insert(Result result) {
        repo.insertGame(result);
    }

    public void update(Result result) {
        repo.update(result);
    }
}
