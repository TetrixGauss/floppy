package com.ath.floppy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ath.floppy.models.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private ArrayList<Result> results;
    OnBottomReachedListener onBottomReachedListener;
    int id;
    int position;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardview;
        TextView rName;
        ImageView back_image;
        int pos;

        ToggleButton favorite;
        ToggleButton wish;


        public MyViewHolder(View itemView) {
            super(itemView);

            this.rName = (TextView) itemView.findViewById(R.id.gameTitle);
            this.back_image = itemView.findViewById(R.id.game_image);
            this.cardview = itemView.findViewById(R.id.card_view);
            this.favorite = itemView.findViewById(R.id.favorites_btn);
            this.wish = itemView.findViewById(R.id.wishlist_btn);

        }
    }

    public Adapter(ArrayList<Result> data) {
        this.results = data;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_layout, parent, false);

        CardView cardView = view.findViewById(R.id.card_view);
        cardView.setOnClickListener(MainActivity.myOnClickListener);

        ToggleButton favorite = view.findViewById(R.id.favorites_btn);
        ToggleButton wish = view.findViewById(R.id.wishlist_btn);

        favorite.setOnCheckedChangeListener(MainActivity.toggleFavoritesListener);
        wish.setOnCheckedChangeListener(MainActivity.toggleWishListListener);


        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView rName = holder.rName;
        ImageView imview = holder.back_image;
        Context context = imview.getContext();

        ToggleButton favorite = holder.favorite;
        ToggleButton wish = holder.wish;
        Boolean favorite_flag = results.get(listPosition).isFavorite();
        Boolean wish_flag = results.get(listPosition).isWish();

        rName.setText(results.get(listPosition).getName());
        String urlSrt = results.get(listPosition).getBackgroundImage();
        Picasso.with(context)
                .load(urlSrt)
                .centerCrop()
                .fit()
                .into(imview);
        if (results.get(listPosition).isFavorite()){
            favorite.setBackgroundResource(R.drawable.ic_favorites_checked);
        }else {
            favorite.setBackgroundResource(R.drawable.ic_favorites);
        }

        if (results.get(listPosition).isWish()){
            wish.setBackgroundResource(R.drawable.ic_wishlist_checked);
        }else {
            wish.setBackgroundResource(R.drawable.ic_wishlist);
        }

        position = listPosition;
        holder.cardview.setTag(position);
        if (position == results.size() - 1){

//                onBottomReachedListener.onBottomReached(position);

        }

    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }


    public void setResults(List<Result> resultsvm) {
        this.results = (ArrayList<Result>) resultsvm;
        notifyDataSetChanged();
    }

    public ArrayList<Result> getResults() {
        return results;
    }

    @Override
    public int getItemCount() {
        if (results != null) {
            return results.size();
        }else {
            return 0;
        }
    }
}
