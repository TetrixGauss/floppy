package com.ath.floppy;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.ath.floppy.models.Result;
import com.ath.floppy.models.ServerResponse;
import com.ath.floppy.view_models.ResultViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GameActivity extends FragmentActivity {
    ResultViewModel resultViewModel;
    String jsonStr;
    int pos;
    TextView game_title;
    TextView description;
    ShapeableImageView imageView;
    Button back_arrow;
    ToggleButton favorite;
    ToggleButton wish;
    Boolean favorite_is_checked;
    Boolean wish_is_checked;

    ServerResponse serverResponse;
    Result result;
    static ArrayList<Result> result_list = new ArrayList<>();

    private static int NUM_PAGES = 1;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_details_layout);
        back_arrow = findViewById(R.id.arrow);

        Intent intent = getIntent();
        Gson gson = new GsonBuilder().create();
        jsonStr = (String) intent.getSerializableExtra("response");
        pos = intent.getIntExtra("position", 0);

        serverResponse = gson.fromJson(jsonStr, ServerResponse.class);
        result_list = serverResponse.getResults();
        result = result_list.get(pos);


        game_title = findViewById(R.id.gameTitle);
        game_title.setText(result.getName());

        imageView = findViewById(R.id.header);
        Context context = imageView.getContext();
        String urlSrt = result.getBackgroundImage();
        Picasso.with(context)
                .load(urlSrt)
                .centerCrop()
                .fit()
                .into(imageView);

        float radius = getResources().getDimension(R.dimen.default_corner_radius);
        imageView.setShapeAppearanceModel(imageView.getShapeAppearanceModel()
                .toBuilder()
                .setBottomRightCorner(CornerFamily.ROUNDED,radius)
                .setBottomLeftCorner(CornerFamily.ROUNDED,radius)
                .build());

        resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);

        favorite =  findViewById(R.id.favorite);
        wish = findViewById(R.id.wish);

        if (result.isFavorite()){
            favorite.setBackgroundResource(R.drawable.ic_favorites_checked);
            favorite.isChecked();
        }else {
            favorite.setBackgroundResource(R.drawable.ic_favorites);
        }

        if (result.isWish()){
            wish.setBackgroundResource(R.drawable.ic_wishlist_checked);
            wish.isChecked();
        }else {
            wish.setBackgroundResource(R.drawable.ic_wishlist);
        }

        favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean is_Checked) {
                if (is_Checked) {
                    // The toggle is enabled
                    buttonView.setBackgroundResource(R.drawable.ic_favorites);
                    favorite_is_checked = true;
                    result.setFavorite(true);

                } else {
                    // The toggle is disabled
                    favorite_is_checked = false;
                    buttonView.setBackgroundResource(R.drawable.ic_favorites_checked);
                    result.setFavorite(false);
                }
            }
        });

        wish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean is_Checked) {
                if (is_Checked) {
                    // The toggle is enabled
                    wish_is_checked = true;
                    result.setWish(true);
                    buttonView.setBackgroundResource(R.drawable.ic_wishlist);
                } else {
                    // The toggle is disabled
                    wish_is_checked = false;
                    result.setWish(false);
                    buttonView.setBackgroundResource(R.drawable.ic_wishlist_checked);
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private boolean isConnected() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null) return false;

            return networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public void goBack (View v){
        super.onBackPressed();
    }
}
