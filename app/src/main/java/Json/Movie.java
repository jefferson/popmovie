package Json;

import java.io.Serializable;

/**
 * Created by JeffersonAlves on 26/09/2015.
 */
public class Movie implements Serializable {
    public Boolean adult;
    public String backdrop_path;
    public Integer[] genre_ids;
    public Integer id;
    public String original_language;
    public String original_title;
    public String overview;
    public String release_date;
    public String poster_path;
    public float popularit;
    public String title;
    public Boolean video;
    public float vote_average;
    public Integer vote_count;
}
