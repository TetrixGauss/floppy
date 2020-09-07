package com.ath.floppy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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
import com.ath.floppy.models.Result;
import com.ath.floppy.models.ServerResponse;
import com.ath.floppy.view_models.PlatformViewModel;
import com.ath.floppy.view_models.ResultViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;
import static com.ath.floppy.PaginationListener.PAGE_START;

public class MainActivity extends AbstractActivity {

    public static RecyclerView.OnClickListener myOnClickListener;
    public static CompoundButton.OnCheckedChangeListener toggleFavoritesListener;
    public static CompoundButton.OnCheckedChangeListener toggleWishListListener;
    ServerResponse server;
    ServerResponse servertmp = new ServerResponse();
    SharedPreferences prefs = null;

    ResultViewModel resultViewModel;
    PlatformViewModel platformViewModel;
//    RatingsViewModel ratingsViewModel;
    Adapter adaptre = null;

    ArrayList<Result> data;
    ArrayList<Result> dataDB;
    ArrayList<Platform> platformData;
//    private SearchView searchView;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    private int currentPage = PAGE_START;
    int itemCount = 0;
    int dx_cust;
    int dy_cust;
    private int scrollPosition = 0;
    private boolean shouldKeepScrollPosition = true;

    String json;
    String jsonServer;

    ToggleButton favorite_toggle;
    ToggleButton wish_toggle;
    Boolean favorite_is_checked ;
    Boolean wish_is_checked;

    Button userProfile;

    SearchView search;
    String url = "https://api.rawg.io/api/games";
    String previous_url; // = "https://api.rawg.io/api/games";
    String next_url;

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initialiseLayout() {
        prefs = getSharedPreferences("com.ath.floppy", MODE_PRIVATE);

//        getSupportActionBar().setCustomView(R.layout.main_toolbar);
        userProfile = findViewById(R.id.user_btn);

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        search = findViewById(R.id.search);
        search.setBackgroundResource(R.drawable.searchview_rounded);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        final int dx_cust;
//        int dy_cust;


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(shouldKeepScrollPosition) {
                    scrollPosition += dy;

                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                adapter = (Adapter) recyclerView.getAdapter();
//                recyclerView.getScrollState();
//               if ( scrollPosition == recyclerView.getScrollState() )
            }

        });



//        adapter = (Adapter) recyclerView.getAdapter();
//        if (adapter != null) {
//            adapter.setOnBottomReachedListener(new OnBottomReachedListener() {
//                @Override
//                public void onBottomReached(int position) {
//                    //your code goes here
//                    loadMoreGames(next_url);
//                }
//            });
//        }
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (previous_url != url) {
//                    loadMoreGames(url);
//                }
//            }
//        });



        View view = getLayoutInflater().inflate(R.layout.game_layout,null);


        favorite_toggle = view.findViewById(R.id.favorites_btn);
//        favorite_toggle.setBackgroundResource(R.drawable.ic_favorites);
//        favorite_toggle.setText("");
        wish_toggle = view.findViewById(R.id.wishlist_btn);
//        wish_toggle.setText("");


        myOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                if (isConnected()){
                    intent.putExtra("response",jsonServer ); //  json
                } else {
                    Gson gson = new GsonBuilder().create();
                    servertmp.setResults(dataDB);
                    String jsonData = gson.toJson(servertmp);
                    intent.putExtra("response", jsonData);
                }

                int pos = (int) v.getTag();
                intent.putExtra("position", pos);
                shouldKeepScrollPosition = false;
                startActivity(intent);
            }
        };

        toggleFavoritesListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    favorite_is_checked = true;
                    buttonView.setBackgroundResource(R.drawable.ic_favorites_checked);
                } else {
                    favorite_is_checked = false;
                    // The toggle is disabled
                    buttonView.setBackgroundResource(R.drawable.ic_favorites);
                }
            }
        };

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

        recyclerView.scrollToPosition(scrollPosition);

        resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);


        if (prefs.getBoolean("initiation", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");
            if (isConnected()) {
                fetchGames(url);
            }else {
                Toast.makeText(MainActivity.this, "Please check your internet connection and restart the TV-Guide!",
                        Toast.LENGTH_SHORT).show();
            }
            // record the fact that the app has been started at least once
            prefs.edit().putBoolean("initiation", false).apply();
        }else {
            if (isConnected()) {

                fetchGames(url);

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


    //////////////////////////////////////////////////////////////////

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

    private void fetchGames(String str) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String source = str;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, source,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("COMMUNICATION", response);

                        server = new Gson().fromJson(response, ServerResponse.class);

                        next_url = server.getNext();
                        previous_url = server.getPrevious();
//                        url = server.getNext();
                        data = (ArrayList<Result>) server.getResults();
                        dataDB = data;
//                        int resid = 1;
//                        int prid = 1;

                        for (Result res : data
                        ) {
                            Log.d("COMMUNICATION", res.toString());

                            resultViewModel.insert(res);
                            platformData = res.getPlatforms();
//                            for (Platform pr : platformData){
//                                platformViewModel.insert(pr);
//                            }
                        }

                        Gson gson = new GsonBuilder().create();

                        json = gson.toJson(server.getResults());
                        jsonServer = gson.toJson(server);

                        Log.d("RESULTS", json);
                        if (previous_url == null) {
                            adapter = new Adapter(data);
                        }else {
                            adapter.setResults(data);
                        }

                        recyclerView.setAdapter(adapter);
//                        recyclerView.addOnScrollListener(new PaginationListener((GridLayoutManager) layoutManager) {
//                            @Override
//                            protected void loadMoreItems() {
//                                isLoading = true;
//                                currentPage++;
////                                doApiCall();
//                            }
//                            @Override
//                            public boolean isLastPage() {
//                                return isLastPage;
//                            }
//                            @Override
//                            public boolean isLoading() {
//                                return isLoading;
//                            }
//
//
//                        });
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

        } else {
            Toast.makeText(MainActivity.this, "Please check your internet connection and try again!",
                    Toast.LENGTH_SHORT).show();
//            resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);
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