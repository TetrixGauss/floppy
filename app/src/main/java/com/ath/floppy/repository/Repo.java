package com.ath.floppy.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.ath.floppy.db.GameDataBase;
import com.ath.floppy.db.PlatformDAO;
import com.ath.floppy.db.ResultDAO;
import com.ath.floppy.models.Platform;
import com.ath.floppy.models.Result;

import java.util.List;

public class Repo {
    private ResultDAO resultDAO;
    private PlatformDAO platformDAO;
    @NonNull
    private LiveData<List<Result>> results;
    @NonNull
    private LiveData<List<Result>> gameWithId;
    int gameId;
    @NonNull
    private LiveData<List<Platform>> platforms;
    @NonNull
    private LiveData<List<Platform>> getPlatformsWithId;
    int id;


    public Repo(Application application) {
        GameDataBase db = GameDataBase.getInstance(application);
        resultDAO = db.resultDAO();
        results   = resultDAO.getAllResults();
        platformDAO = db.platformDAO();
        platforms   = platformDAO.getAllPlatforms();
//        programsWithId = programDAO.getAllProgWithChannelId(id);

    }

    public LiveData<List<Result>> getAllResults() {
        return results;
    }

    public LiveData<List<Platform>> getAllPlatforms() {
        return platforms;
    }

    public LiveData<List<Platform>> getPlatformsWithId(int id) {
        return platformDAO.getPlatformsWithId(id);
    }

    public LiveData<List<Result>> getGamesWithId(int id){
        return resultDAO.getGamesWithId(id);
    }
    public void insertGame (Result result) {
        new insertAsyncTaskforResult(resultDAO).execute(result);
    }

    public void insertPlatform (Platform platform) {
        new insertAsyncTaskforPlatform(platformDAO).execute(platform);
    }

    public void update(Result result){
        new UpdateChannelAsyncTask(resultDAO).execute(result);
    }


    private static class insertAsyncTaskforResult extends AsyncTask<Result, Void, Void> {
        private ResultDAO mAsyncTaskDAO;

        public insertAsyncTaskforResult(ResultDAO resultDAO) {
            mAsyncTaskDAO = resultDAO;
        }

        @Override
        protected Void doInBackground(final Result... results) {
            mAsyncTaskDAO.insert(results[0]);
            return null;
        }
    }

    private static class insertAsyncTaskforPlatform extends AsyncTask<Platform, Void, Void> {
        private PlatformDAO mAsyncTaskDAO;

        public insertAsyncTaskforPlatform(PlatformDAO platformDAO) {
            mAsyncTaskDAO = platformDAO;
        }

        @Override
        protected Void doInBackground(final Platform... platforms) {
            mAsyncTaskDAO.insert(platforms[0]);
            return null;
        }
    }

    private static class UpdateChannelAsyncTask extends AsyncTask<Result, Void, Void>{

        private ResultDAO resultDAO;
        public UpdateChannelAsyncTask(ResultDAO resultDAO) {
            this.resultDAO = resultDAO;
        }

        @Override
        protected Void doInBackground(Result... results) {
            resultDAO.update(results[0]);
            return null;
        }
    }
}
