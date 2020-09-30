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

import com.ath.floppy.models.Platform;
import com.ath.floppy.models.Platforms;
import com.ath.floppy.models.Ratings;
import com.ath.floppy.models.Result;
import com.ath.floppy.models.ServerResponse;
import com.ath.floppy.view_models.PlatformViewModel;
import com.ath.floppy.view_models.ResultViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class GameActivity extends FragmentActivity {
    ResultViewModel resultViewModel;
    PlatformViewModel platformViewModel;
    String jsonStr;
    String jsonStrPlatforms;
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
    static Result result;
    ArrayList<Result> result_list;
    Platforms platform_list;
    double rating;
    TextView ratingTV;

    String platforms;

    ArrayList<String> gameIds;
    ArrayList<String> gameIds_tmp;
    String gameID;
    String favoriteID;
    public static ParseObject games_entity;
    ParseObject favorites_entity;

    private static int NUM_PAGES = 1;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_details_layout);
        back_arrow = findViewById(R.id.arrow);
        game_title = findViewById(R.id.gameTitle);
        imageView = findViewById(R.id.header);
        float radius = getResources().getDimension(R.dimen.default_corner_radius);
        imageView.setShapeAppearanceModel(imageView.getShapeAppearanceModel()
                .toBuilder()
                .setBottomRightCorner(CornerFamily.ROUNDED, radius)
                .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
                .build());

        Intent intent = getIntent();

        String from = intent.getStringExtra("from");
//        if (from.equals("main")) {
            Gson gson = new GsonBuilder().create();
            jsonStr = (String) intent.getSerializableExtra("response");
            jsonStrPlatforms = (String) intent.getSerializableExtra("response_platforms");

            pos = intent.getIntExtra("position", 0);

            serverResponse = gson.fromJson(jsonStr, ServerResponse.class);
//        platform_list = gson.fromJson(jsonStrPlatforms, Platforms.class);
            if (result_list == null) {
                result_list = serverResponse.getResults();
            } else {
                result_list.addAll(serverResponse.getResults());
            }
            result = result_list.get(pos);
            rating = result.getRating();

//        ratingTV = findViewById(R.id.)
            game_title.setText(result.getName());

            Context context = imageView.getContext();
            String urlSrt = result.getBackgroundImage();
            Picasso.with(context)
                    .load(urlSrt)
                    .centerCrop()
                    .fit()
                    .into(imageView);

            if (isConnected()) {
                createGameObject();
            }
//        } else { // Favorites
//
//        }

        description = findViewById(R.id.platforms_info);

//        for (Platform pl : platform_list.getPlatforms()){
//            platforms = pl.getName() + ", ";
//        }

//        description.setText(platforms);

        resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);

        favorite = findViewById(R.id.favorite);
        wish = findViewById(R.id.wish);

        if (result.isFavorite()) {
            favorite.setBackgroundResource(R.drawable.ic_favorites_checked);
            addFavoriteGame();
            favorite.isChecked();
        } else {
            favorite.setBackgroundResource(R.drawable.ic_favorites);
        }

//        if (result.isWish()) {
//            wish.setBackgroundResource(R.drawable.ic_wishlist_checked);
//            wish.isChecked();
//        } else {
//            wish.setBackgroundResource(R.drawable.ic_wishlist);
//        }

//        if (from.equals("favorite")) {
//            favorite.setBackgroundResource(R.drawable.ic_favorites_checked);
            favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean is_Checked) {
                    if (is_Checked) {
                        // The toggle is enabled
                        buttonView.setBackgroundResource(R.drawable.ic_favorites_checked);
                        favorite_is_checked = true;
                        result.setFavorite(true);
                        addFavoriteGame();
                    } else {
                        // The toggle is disabled
                        favorite_is_checked = false;
                        buttonView.setBackgroundResource(R.drawable.ic_favorites);
                        result.setFavorite(false);
                        deleteFavoriteGame();
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
//        }


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

            return networkInfo.isConnected(); // && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public void goBack(View v) {
        super.onBackPressed();
    }

    public void createGameObject() {

        if (isGameInServer()) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");
            try {
                List<ParseObject> games = query.find();
                for (ParseObject game : games) {
                    if (game.getString("gameName").equals(result.getName())) {
                        gameID = game.getObjectId();
                        games_entity = game;
                        break;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            games_entity = new ParseObject("Games");

            games_entity.put("gameName", result.getName());
            games_entity.put("releaseDate", result.getReleased());
            games_entity.put("rating", result.getRating());
            games_entity.put("gamePic", result.getBackgroundImage());
            gameID = games_entity.getObjectId();
            // Saves the new object.
            // Notice that the SaveCallback is totally optional!
            games_entity.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    // Here you can handle errors, if thrown. Otherwise, "e" should be null
                }
            });
        }
    }

    public static boolean isGameInServer() {

        // Creates a new ParseQuery object to help us fetch MyCustomClass objects
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");
        boolean flag = false;
//        final boolean[] find = new boolean[1];

//        // Fetches data synchronously
        try {
            List<ParseObject> games = query.find();
            for (ParseObject game : games) {
//                System.out.println("Object found " + result.getObjectId());
                if (game.getString("gameName").equals(result.getName())) {
                    flag = true;
                    break;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public void addFavoriteGame() {

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {

            ParseRelation<ParseObject> relation = currentUser
                    .getRelation("favoriteGames");
            relation.add(games_entity);

            // Saves the object.
            // Notice that the SaveCallback is totally optional!
            currentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    // Here you can handle errors, if thrown. Otherwise, "e" should be null
                }
            });
        }

    }

    public void deleteFavoriteGame(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {

            ParseRelation<ParseObject> relation = currentUser
                    .getRelation("favoriteGames");
            relation.remove(games_entity);

            // Saves the object.
            // Notice that the SaveCallback is totally optional!
            currentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    // Here you can handle errors, if thrown. Otherwise, "e" should be null
                }
            });
        }
    }
}
