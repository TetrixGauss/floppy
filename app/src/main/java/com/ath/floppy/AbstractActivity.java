package com.ath.floppy;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class AbstractActivity extends AppCompatActivity {

    @LayoutRes
    public abstract int getLayout();

    public abstract void initialiseLayout();

    public abstract void runOperation();

    public abstract void stopOperation();

    public abstract void destroyLayout();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initialiseLayout();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        runOperation();
    }


    @Override
    protected void onPause() {
        stopOperation();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        destroyLayout();
        super.onDestroy();
    }

}
