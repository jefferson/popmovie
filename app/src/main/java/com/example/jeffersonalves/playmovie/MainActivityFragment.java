package com.example.jeffersonalves.playmovie;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import Json.Movie;
import Json.Movies;
import Rest.TheMovieDbApi;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private MovieAdapater mAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        UpdateMovieAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.movie_recycler_view);

        recyclerView.setHasFixedSize(true);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),
                getActivity().getResources().getInteger(R.integer.GRID_NUM_COLUMNS));

        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MovieAdapater(getContext(), new Movie[]{});
        recyclerView.setAdapter(mAdapter);


        return root;
    }

    public void UpdateMovieAdapter(){

        MovieAsyncTask movieAsyncTask = new MovieAsyncTask();
        movieAsyncTask.execute();

    }

    public class MovieAsyncTask extends AsyncTask<Void, Void, Movie[]> {

        private final String LOG_CAT = MovieAsyncTask.class.getSimpleName();

        @Override
        protected Movie[] doInBackground(Void... params) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TheMovieDbApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final TheMovieDbApi theMovieDbApi = retrofit.create(TheMovieDbApi.class);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String orderBy = sharedPreferences.getString(getString(R.string.pref_short_key),
                    getString(R.string.pref_short_default_value));

            Call<Movies> call = theMovieDbApi.GetUpComing(getString(R.string.themoviedb_api_key), orderBy);

            try {

                Movies movies = call.execute().body();
                return  movies.results;

            } catch (IOException e) {
                e.printStackTrace();
                Log.d(LOG_CAT, e.getMessage());
            }

            return new Movie[]{};
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            if (movies.length > 0) {
                mAdapter.addAll(movies);
            }
        }
    }
}
