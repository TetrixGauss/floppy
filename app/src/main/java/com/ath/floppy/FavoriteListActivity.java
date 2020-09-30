package com.ath.floppy;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ath.floppy.models.Result;
import com.ath.floppy.models.ServerResponse;
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

public class FavoriteListActivity extends AppCompatActivity {// implements SwipeRefreshLayout.OnRefreshListener {

    ArrayList<Result> results = new ArrayList<Result>();
    ServerResponse servertmp = new ServerResponse();
    Result res;

    Button userProfile;

    ToggleButton favorite_toggle;
    ToggleButton wish_toggle;
    Boolean favorite_is_checked ;
    Boolean wish_is_checked;

    ProgressBar progressBar;

    ResultViewModel resultViewModel;

    SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private ListAdapter adapter;

    public static RecyclerView.OnClickListener myOnClickListener;
    public static CompoundButton.OnCheckedChangeListener toggleFavoritesListener;
    public static CompoundButton.OnCheckedChangeListener toggleWishListListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_wish_list);

//        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        userProfile = findViewById(R.id.user_btn);

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteListActivity.this, UserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

//        if (isConnected()) {
            fetchFavorites();
//        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
//                android.R.color.holo_green_dark,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_blue_dark);
//            mSwipeRefreshLayout.post(new Runnable() {
//
//                @Override
//                public void run() {
//
//                    // Fetching data from server
//                    fetchFavorites();
//                }
//            });

//        }else {
//
//            resultViewModel.getResults().observe(this, new Observer<List<Result>>() {
//
//                @Override
//                public void onChanged(@Nullable final List<Result> results) {
//
//                    ArrayList<Result> dataDB = (ArrayList<Result>) results;
//                    ArrayList<Result> dataTmp = new ArrayList<>();
//                    for (Result r : dataDB){
//                        if (r.isFavorite()){
//                            dataTmp.add(r);
//                        }
//                    }
//                    adapter = new ListAdapter(dataTmp);
//                    adapter.setResults(results);
//                    recyclerView.setAdapter(adapter);
//                }
//            });
//            ArrayList<Result> dataTmp = new ArrayList<>();
//            for (Result r : dataDB){
//                if (r.isFavorite()){
//                    dataTmp.add(r);
//                }
//            }
//        }
        myOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FavoriteListActivity.this, GameActivity.class);

                Gson gson = new GsonBuilder().create();
                servertmp.setResults(results);
                String jsonData = gson.toJson(servertmp);
                intent.putExtra("response", jsonData);


                int pos = (int) v.getTag();
                intent.putExtra("position", pos);
                intent.putExtra("from", "favorites");

                startActivity(intent);
            }
        };

        View view = getLayoutInflater().inflate(R.layout.game_layout,null);

        favorite_toggle = view.findViewById(R.id.favorites_btn);
        wish_toggle = view.findViewById(R.id.wishlist_btn);

        toggleFavoritesListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos = (int) buttonView.getTag();
                if (isChecked) {
                    // The toggle is enabled
                    favorite_is_checked = true;
                    results.get(pos).setFavorite(true);
                    buttonView.setBackgroundResource(R.drawable.ic_favorites_checked);
                } else {
                    favorite_is_checked = false;
                    results.get(pos).setFavorite(false);
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

    ///////////////////////////////////////////////////////////////////////////

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

    public void fetchFavorites() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            ParseRelation<ParseObject> relation = currentUser
                    .getRelation("favoriteGames");
            if (relation != null) {
                ParseQuery<ParseObject> query = relation.getQuery();
                List<ParseObject> relatedObjects = null;
                try {
                    relatedObjects = query.find();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (ParseObject parseObject : relatedObjects) {
                    res = new Result();
                    res.setBackgroundImage(parseObject.getString("gamePic"));
                    res.setName(parseObject.getString("gameName"));
                    res.setRating(parseObject.getDouble("rating"));
                    res.setReleased(parseObject.getString("releaseDate"));
                    res.setFavorite(true);
                    results.add(res);
                }
                adapter = new ListAdapter(results);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    public void goBack(View v) {
        super.onBackPressed();
    }

//    @Override
//    public void onRefresh() {
//        mSwipeRefreshLayout.setRefreshing(true);
//        fetchFavorites();
//
//    }
}
