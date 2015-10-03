package Rest;

import Json.MovieDetail;
import Json.Movies;
import Json.Videos;
import Json.Viewers;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by JeffersonAlves on 26/09/2015.
 */
public interface TheMovieDbApi {

    String BASE_URL = "http://api.themoviedb.org";

    /**
     * We need move api_key in this method in app of production
     * Find api_key into resouces strings.
     * @return
     */
    @GET("3/discover/movie")
    Call<Movies> GetUpComing(@Query("api_key") String api_key, @Query("sort_by") String sort_by);

    @GET("3/movie/{id}")
    Call<MovieDetail> GetMovie( @Path("id") Integer id, @Query("api_key") String api_key);

    @GET("3/movie/{id}/videos")
    Call<Videos> GetVideosFromMovie( @Path("id") Integer id, @Query("api_key") String api_key);

    @GET("3/movie/{id}/reviews")
    Call<Viewers> GetReviewsFromMovie( @Path("id") Integer id, @Query("api_key") String api_key);
}
