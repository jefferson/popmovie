package com.example.jeffersonalves.playmovie;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import Json.Movie;
import Json.MovieDetail;
import Json.Videos;
import Json.Viewers;
import Rest.TheMovieDbApi;
import Util.Util;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String LOG_CAT = DetailActivityFragment.class.getSimpleName();
    private final String MOVIE_DETAIL_TAG = "movie_detail_tag";

    private Movie movie;
    private MovieDetail movieDetail;

    private Videos videos;
    private Viewers viewers;

    private VideoAdapter videoAdapter;
    private ViewerAdapter viewerAdapter;

    public DetailActivityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);



        movie = (Movie) getActivity().getIntent().getSerializableExtra(MovieAdapater.MOVIE_TAG);

        TextView textViewTitle = (TextView) root.findViewById(R.id.textViewTitle);
        textViewTitle.setText(movie.original_title);

        ImageView imageView = (ImageView) root.findViewById(R.id.imageViewDetailMovie);
        Picasso.with(getContext()).load(getContext().getString(R.string.themoviedb_base_img_url) + movie.poster_path)
                .into(imageView);

        TextView textViewYear = (TextView) root.findViewById(R.id.textViewYearRelease);

        if(movie.release_date!= null && movie.release_date.length() >= 4)
            textViewYear.setText(movie.release_date.substring(0, 4));
        else
            textViewYear.setText(getString(R.string.no_release_date));

        TextView textViewVotes = (TextView) root.findViewById(R.id.textViewVotes);
        textViewVotes.setText(String.valueOf(movie.vote_average).concat("/").concat(String.valueOf(movie.vote_count)));

        TextView textViewSinopse = (TextView) root.findViewById(R.id.textViewSinopse);
        textViewSinopse.setText(movie.overview);

        VideoTask videoTask = new VideoTask();
        videoTask.execute(movie.id);

        MovieDeitalAsyncTask movieDeitalAsyncTask = new MovieDeitalAsyncTask();
        movieDeitalAsyncTask.execute(movie.id);

        ViewerTask viewerTask = new ViewerTask();
        viewerTask.execute(movie.id);

        return root;
    }

    public void updateVideos(Videos v){

        ScrollView scrollView = (ScrollView) getView().findViewById(R.id.scrollView_detail);
        scrollView.scrollTo(0, 0);
        scrollView.setVerticalScrollBarEnabled(false);

        videos = v;
        videoAdapter = new VideoAdapter(getContext(), v.results);

        ListView listView = (ListView) getView().findViewById(R.id.listViewVideos);
        listView.setAdapter(videoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = videoAdapter.getItem(position).key;
                openVideo(key);
            }
        });

        Util.Tools.calculeHeightListView(listView);
        scrollView.setVerticalScrollBarEnabled(true);
        scrollView.scrollTo(0, 0);
    }

    private void openVideo(String videoUrl){

        if(videoUrl.length() > 0) {
            Uri videoUri = Uri.parse("https://www.youtube.com/watch").buildUpon()
                    .appendQueryParameter("v", videoUrl)
                    .build();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(videoUri);

            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        else{
            Toast.makeText(getContext(), "Video not found!", Toast.LENGTH_SHORT).show();
        }

    }
    public void updateReviews(Viewers v){

        ScrollView scrollView = (ScrollView) getView().findViewById(R.id.scrollView_detail);
        scrollView.scrollTo(0, 0);
        scrollView.setVerticalScrollBarEnabled(false);


        viewers = v;
        viewerAdapter = new ViewerAdapter(getContext(),viewers.results);
        ListView listView = (ListView) getView().findViewById(R.id.listViewReviews);
        listView.setAdapter(viewerAdapter);

        Util.Tools.calculeHeightListView(listView, 50);
        scrollView.setVerticalScrollBarEnabled(true);
        scrollView.scrollTo(0, 0);
        scrollView.requestLayout();



    }

    public void updateRuntime(MovieDetail m) {
        if (m != null)
            UpdateTextRuntime(String.valueOf(m.runtime));
        else
            UpdateTextRuntime(getString(R.string.no_minutes));
    }

    public void UpdateTextRuntime(String runtimeText) {
        TextView runtime = (TextView) getView().findViewById(R.id.textViewRuntime);
        runtime.setText(runtimeText.concat(getString(R.string.minutes_label)));
    }

    public class MovieDeitalAsyncTask extends AsyncTask<Integer, Void, MovieDetail> {

        @Override
        protected MovieDetail doInBackground(Integer... params) {

            if (params.length == 0)
                return null;

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TheMovieDbApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final TheMovieDbApi theMovieDbApi = retrofit.create(TheMovieDbApi.class);
            Call<MovieDetail> call = theMovieDbApi.GetMovie(params[0], getString(R.string.themoviedb_api_key));

            try {

                return call.execute().body();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(MovieDetail movieResult) {
            movieDetail = movieResult;
            updateRuntime(movieDetail);
        }
    }

    public class VideoTask extends AsyncTask<Integer, Void, Videos>{

        @Override
        protected Videos doInBackground(Integer... params) {
            if(params.length == 0 )
                return  null;

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TheMovieDbApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final TheMovieDbApi theMovieDbApi = retrofit.create(TheMovieDbApi.class);
            Call<Videos> call = theMovieDbApi.GetVideosFromMovie(params[0], getString(R.string.themoviedb_api_key));

            try {
                return call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Videos v) {
            updateVideos(v);
        }
    }

    public class ViewerTask extends  AsyncTask<Integer, Void, Viewers>{

        @Override
        protected Viewers doInBackground(Integer... params) {
            if(params.length == 0 )
                return  null;

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TheMovieDbApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final TheMovieDbApi theMovieDbApi = retrofit.create(TheMovieDbApi.class);
            Call<Viewers> call = theMovieDbApi.GetReviewsFromMovie(params[0], getString(R.string.themoviedb_api_key));

            try {
                return call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Viewers v) {
            updateReviews(v);
        }
    }
}
