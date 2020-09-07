package com.ath.floppy.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ath.floppy.models.Platform;
import com.ath.floppy.models.Result;
import com.ath.floppy.repository.Repo;

import java.util.List;


public class PlatformViewModel extends AndroidViewModel {

    private Repo repo;
    private LiveData<List<Platform>> platforms;
    LiveData<List<Platform>> platformsWithId;
    int id;

    public PlatformViewModel(@NonNull Application application) {
        super(application);
        repo = new Repo(application);
        platforms = repo.getAllPlatforms();
    }

    public LiveData<List<Platform>> getPlatforms() {
        return platforms;
    }
    public LiveData<List<Platform>> getPlatformsWithId(int id) {
        return repo.getPlatformsWithId(id);
    }

    public void setPlatforms(LiveData<List<Platform>> platforms) {
        this.platforms = platforms;
    }

    public void insert(Platform program) {
        repo.insertPlatform(program);
    }

    public void update(Result result) {
        repo.update(result);
    }
}
