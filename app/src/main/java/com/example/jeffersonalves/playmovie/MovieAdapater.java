package com.example.jeffersonalves.playmovie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import Json.Movie;

/**
 * Created by JeffersonAlves on 26/09/2015.
 */
public class MovieAdapater extends RecyclerView.Adapter<MovieAdapater.ViewHolder> {

    private Movie[] mDataSet;
    private final Context context;
    public static final String  MOVIE_TAG = "movie_serializable";

    public MovieAdapater(Context context, Movie[] movies) {
        this.mDataSet = movies;
        this.context = context;
    }

    public void addAll(Movie[] m) {
        this.mDataSet = m;
        notifyDataSetChanged();
    }

    @Override
    public MovieAdapater.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_movie, viewGroup, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewMovie);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ViewHolder viewHolder = new ViewHolder(imageView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieAdapater.ViewHolder viewHolder, final int i) {

        Picasso.with(context).load(context.getString(R.string.themoviedb_base_img_url)+""+(mDataSet[i].poster_path))
                .into(viewHolder.imageView);

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDetailActivity(context, mDataSet[i]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    public void ShowDetailActivity(Context context, Movie movie){
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(MOVIE_TAG, movie);
        context.startActivity(intent);
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(ImageView itemView) {
            super(itemView);
            imageView = itemView;
        }

    }
}
