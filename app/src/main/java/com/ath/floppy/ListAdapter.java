package com.ath.floppy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ath.floppy.models.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder>  {
    private ArrayList<Result> favorites;
    int position;
    boolean isLoadingAdded;

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardview;
        TextView rName;
        ImageView back_image;
        int pos;

        ToggleButton favorite;
        ToggleButton wish;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.rName = (TextView) itemView.findViewById(R.id.gameTitle);
            this.back_image = itemView.findViewById(R.id.game_image);
            this.cardview = itemView.findViewById(R.id.card_view);
            this.favorite = itemView.findViewById(R.id.favorites_btn);
            this.wish = itemView.findViewById(R.id.wishlist_btn);
        }
    }

    public ListAdapter (ArrayList<Result> data){
        this.favorites = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_layout, parent, false);

        CardView cardView = view.findViewById(R.id.card_view);
        cardView.setOnClickListener(FavoriteListActivity.myOnClickListener);

        ToggleButton favorite = view.findViewById(R.id.favorites_btn);
        ToggleButton wish = view.findViewById(R.id.wishlist_btn);

        favorite.setOnCheckedChangeListener(FavoriteListActivity.toggleFavoritesListener);
        wish.setOnCheckedChangeListener(FavoriteListActivity.toggleWishListListener);

        ListAdapter.MyViewHolder myViewHolder = new ListAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int listPosition) {
        TextView rName = holder.rName;
        ImageView imview = holder.back_image;
        Context context = imview.getContext();

        ToggleButton favorite = holder.favorite;
        ToggleButton wish = holder.wish;
        Boolean favorite_flag = favorites.get(listPosition).isFavorite();
        Boolean wish_flag = favorites.get(listPosition).isWish();

        rName.setText(favorites.get(listPosition).getName());
        String urlSrt = favorites.get(listPosition).getBackgroundImage();
        Picasso.with(context)
                .load(urlSrt)
                .centerCrop()
                .fit()
                .into(imview);

        if (favorite_flag){
            favorite.setBackgroundResource(R.drawable.ic_favorites_checked);
        }else {
            favorite.setBackgroundResource(R.drawable.ic_favorites);
        }

//        if (favorites.get(listPosition).isWish()){
//            wish.setBackgroundResource(R.drawable.ic_wishlist_checked);
//        }else {
//            wish.setBackgroundResource(R.drawable.ic_wishlist);
//        }

        position = listPosition;
        holder.cardview.setTag(position);
        holder.favorite.setTag(position);
        if (position == favorites.size() - 1){

        }
    }

    public void setResults(List<Result> favoritesvm) {
        this.favorites = (ArrayList<Result>) favoritesvm;
        notifyDataSetChanged();
    }

    public ArrayList<Result> getResults() {
        return favorites;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == favorites.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public int getItemCount() {
        if (favorites != null) {
            return favorites.size();
        }else {
            return 0;
        }
    }

    public void add(Result mc) {
        favorites.add(mc);
        notifyItemInserted(favorites.size() - 1);
    }

    public void addAll(ArrayList < Result > favorites) {
        for (Result res: favorites) {
            add(res);
        }
    }

    public void remove(Result result) {
        int position = favorites.indexOf(result);
        if (position > -1) {
            favorites.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Result());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = favorites.size() - 1;
        Result item = getItem(position);
        if (item != null) {
            favorites.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Result getItem(int position) {
        return favorites.get(position);
    }


}
