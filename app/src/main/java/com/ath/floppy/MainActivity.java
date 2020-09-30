package com.ath.floppy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ath.floppy.models.Platform;
import com.ath.floppy.models.Platforms;
import com.ath.floppy.models.Result;
import com.ath.floppy.models.ServerResponse;
import com.ath.floppy.view_models.PlatformViewModel;
import com.ath.floppy.view_models.ResultViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;
import static com.ath.floppy.PaginationListener.PAGE_START;

public class MainActivity extends AbstractActivity {

    private static final float TOTAL_PAGES = 175000;
    public static RecyclerView.OnClickListener myOnClickListener;
    public static CompoundButton.OnCheckedChangeListener toggleFavoritesListener;
    public static CompoundButton.OnCheckedChangeListener toggleWishListListener;
    ServerResponse server;
    ServerResponse servertmp = new ServerResponse();
    ArrayList<Platform> platformResponse = new ArrayList<Platform>();

    SharedPreferences prefs = null;

    ResultViewModel resultViewModel;
    PlatformViewModel platformViewModel;

        Adapter adaptre = null;

    ArrayList<Result> data;
    ArrayList<Result> dataDB;
    ArrayList<Platform> dataDBPlatforms;
//    Platforms platformsData;
    ArrayList<Platform> platformData;
//    private SearchView searchView;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    private int currentPage = PAGE_START;

    String json;
    String jsonServer;

    ToggleButton favorite_toggle;
    ToggleButton wish_toggle;
    Boolean favorite_is_checked ;
    Boolean wish_is_checked;

    Button userProfile;

    SearchView search;
    String url = "https://api.rawg.io/api/games";
    String first_url = "https://api.rawg.io/api/games";
    String previous_url; // = "https://api.rawg.io/api/games";
    String next_url;

    ProgressBar progressBar;
    Boolean first_time;

    List<ParseObject> games = null;

    public static ParseObject games_entity;

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initialiseLayout() {
        prefs = getSharedPreferences("com.ath.floppy", MODE_PRIVATE);
        first_time = true;

//        getSupportActionBar().setCustomView(R.layout.main_toolbar);
        userProfile = findViewById(R.id.user_btn);

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()) {
                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }else {
                    Toast.makeText(getApplicationContext(), "Connect to the Internet to have access!", Toast.LENGTH_LONG).show();
                }
            }
        });

        search = findViewById(R.id.search);
        search.setBackgroundResource(R.drawable.searchview_rounded);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        progressBar = findViewById(R.id.progress_bar);

        View view = getLayoutInflater().inflate(R.layout.game_layout,null);

        favorite_toggle = view.findViewById(R.id.favorites_btn);
        wish_toggle = view.findViewById(R.id.wishlist_btn);

        myOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, GameActivity.class);

                Gson gson = new GsonBuilder().create();
                servertmp.setResults(dataDB);
                String jsonData = gson.toJson(servertmp);
                intent.putExtra("response", jsonData);
//                platformResponse.addAll(platformsData.getPlatforms());
                String jsonDataPlatforms = gson.toJson(platformData);
                intent.putExtra("response_platforms", jsonDataPlatforms);


                int pos = (int) v.getTag();
                intent.putExtra("position", pos);
                intent.putExtra("from", "main");

                startActivity(intent);
            }
        };

        toggleFavoritesListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos = (int) buttonView.getTag();
                if (isChecked) {
                    // The toggle is enabled
                    favorite_is_checked = true;
                    dataDB.get(pos).setFavorite(true);

                    buttonView.setBackgroundResource(R.drawable.ic_favorites_checked);
                } else {
                    favorite_is_checked = false;
                    dataDB.get(pos).setFavorite(false);
                    // The toggle is disabled
                    buttonView.setBackgroundResource(R.drawable.ic_favorites);
//                    createGameObject();
                }
            }
        };

//        toggleFavoritesListener.onCheckedChanged(favorite_toggle,true);
        toggleWishListListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    wish_is_checked = true;
                    buttonView.setBackgroundResource(R.drawable.ic_wishlist_checked);
                } else {
                    // The toggle is disabled
                    wish_is_checked = false;
                    buttonView.setBackgroundResource(R.drawable.ic_wishlist);
                }
            }
        };

    }

    @Override
    public void runOperation() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.addOnScrollListener(new PaginationListener((GridLayoutManager) layoutManager) {
            @Override
            protected void loadMoreItems() {
                progressBar.setVisibility(View.VISIBLE);
                isLoading = true;
                //Increment page index to load the next one
                currentPage += 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);
        platformViewModel = ViewModelProviders.of(this).get(PlatformViewModel.class);


        if (prefs.getBoolean("initiation", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");
            if (isConnected()) {
                loadFirstPage();
            }else {
                Toast.makeText(MainActivity.this, "Please check your internet connection and restart the TV-Guide!",
                        Toast.LENGTH_SHORT).show();
            }
            // record the fact that the app has been started at least once
            prefs.edit().putBoolean("initiation", false).apply();
        }else {
            if (isConnected()) {
                getFavorites();
                if (first_time) {
                    loadFirstPage();
                    first_time = false;
                }


            } else {
                Toast.makeText(MainActivity.this, "Please check your internet connection and try again!",
                        Toast.LENGTH_SHORT).show();

                resultViewModel.getResults().observe(this, new Observer<List<Result>>() {

                    @Override
                    public void onChanged(@Nullable final List<Result> results) {

                        dataDB = (ArrayList<Result>) results;
                        adaptre = new Adapter(dataDB);
                        adaptre.setResults(results);
                        recyclerView.setAdapter(adaptre);
                    }
                });
            }
        }
    }

    @Override
    public void stopOperation() {

    }

    @Override
    public void destroyLayout() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //////////////////////////////////////////////////////////////////

    private boolean isConnected() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null) return false;

            return networkInfo.isConnected(); // && networkInfo.getType() == ConnectivityManager.TYPE_WIFI ;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return false;
    }

        private void fetchGames(String first_url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String source = first_url;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, first_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("COMMUNICATION", response);

                        server = new Gson().fromJson(response, ServerResponse.class);

                        next_url = server.getNext();
                        previous_url = server.getPrevious();
                        data = (ArrayList<Result>) server.getResults();
//                        for (Result res: data){
//                            if (isFavorite(res)){
//                                res.setFavorite(true);
//                            }else {
//                                res.setFavorite(false);
//                            }
//                        }
//                        if (previous_url == null){
//                            dataDB = data;
//                        }else {
//                            dataDB.addAll(data);
//                        }

                        for (Result res : data) { //dataDB
                            if (isFavorite(res)){
                                res.setFavorite(true);
                            }else {
                                res.setFavorite(false);
                            }
                            Log.d("COMMUNICATION", res.toString());

                            resultViewModel.insert(res);
                            platformData = res.getPlatforms();

//                            for (Platform pr : platformData ){ //platformsData.getPlatforms()
//                                platformViewModel.insert(pr);
//                                pr.setGame_id(res.getId());
//                                Log.d("PLATFORMS", pr.toString());
//
//                                pr.setId(platformData.indexOf(pr));
//                            }
                        }
                        if (previous_url == null){
                            dataDB = data;
                        }else {
                            dataDB.addAll(data);
                        }

//                        Gson gson = new GsonBuilder().create();

//                        json = gson.toJson(server.getResults());
//                        for (Result res: server.getResults()){
////                            res.getPlatforms();
//                            String jsonPlat = gson.toJson(res.getPlatforms());
//
//                            Log.d("plats", jsonPlat);
//                        }
//                        String jsonPlat = gson.toJson(server.getResults())
//                        jsonServer = gson.toJson(server);

//                        Log.d("RESULTS", json);
                        if (previous_url == null) {
                            adapter = new Adapter(data);
                        }else {
                            for (Result r: data){
                                adapter.getResults().add(r);
                            }
                        }

                        recyclerView.setAdapter(adapter);
                        recyclerView.scrollToPosition(adapter.getItemCount() - 40);

                    }
                }, new Response.ErrorListener() {
            //            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("COMMUNICATION", error.getMessage());

                if( error instanceof NetworkError) {
                    //handle your network error here.
                    Toast.makeText(getApplicationContext(), "Please check your internet connection and try again!", Toast.LENGTH_LONG).show();
                } else if( error instanceof ServerError) {
                    //handle if server error occurs with 5** status code
                    Log.d("COMMUNICATION", error.getMessage());
                    Toast.makeText(getApplicationContext(), "There was an error with the server! Please try again!", Toast.LENGTH_SHORT).show();
                } else if( error instanceof AuthFailureError) {
                    //handle if authFailure occurs.This is generally because of invalid credentials
                    Log.d("COMMUNICATION", error.getMessage());
                    Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                } else if( error instanceof ParseError) {
                    //handle if the volley is unable to parse the response data.
                    Log.d("COMMUNICATION", error.getMessage());
                    Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                } else if( error instanceof TimeoutError) {
                    //handle if socket time out is occurred.
                    Log.d("COMMUNICATION", error.getMessage());
                    Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_LONG).show();
                    fetchGames(source);
                }
            }
        });

        queue.add(stringRequest);

    }

    private void loadMoreGames (String url){
        if (isConnected()) {
            fetchGames(url);
            isLoading = false;
        } else {
            Toast.makeText(MainActivity.this, "Please check your internet connection and try again!",
                    Toast.LENGTH_SHORT).show();
            resultViewModel.getResults().observe(this, new Observer<List<Result>>() {

                @Override
                public void onChanged(@Nullable final List<Result> results) {

                    dataDB = (ArrayList<Result>) results;
                    adaptre = new Adapter(dataDB);
                    adaptre.setResults(results);
                    recyclerView.setAdapter(adaptre);
                }
            });

        }
    }

    private void loadFirstPage() {
        fetchGames(first_url);
        progressBar.setVisibility(View.GONE);
    }

    private void loadNextPage() {
        loadMoreGames(next_url);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void getFavorites(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseRelation<ParseObject> relation = currentUser
                .getRelation("favoriteGames");
//            List<ParseObject> games = null;
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("favoriteGames");
//            query.whereEqualTo("gameName", res.getName());
        try {
            games = relation.getQuery().find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean isFavorite(Result res) {
        boolean flag = false;

            for (ParseObject game : games) {
                if (game.getString("gameName").equals(res.getName())) {
                    flag = true;
                    break;
                }
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