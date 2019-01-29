package azuka.com.cataloguemovie.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ivana Situmorang on 1/24/2019.
 */
public class Movie {
    @SerializedName("id")
    @Expose private String movieId;

    @SerializedName("poster_path")
    @Expose private String posterPath;

    @SerializedName("backdrop_path")
    @Expose private String backdropPath;

    @SerializedName("original_title")
    @Expose private String title;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("release_date")
    @Expose private String releaseDate;

    @SerializedName("original_language")
    @Expose private String language;

    @SerializedName("tagline")
    @Expose private String tagline;

    @SerializedName("vote_average")
    @Expose private float rating;

    @SerializedName("runtime")
    @Expose private int duration;

    @SerializedName("genres")
    @Expose private List<Genre> genres;

    public String getMovieId() {
        return movieId;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getLanguage() {
        return language;
    }

    public String getTagline() {
        return tagline;
    }

    public float getRating() {
        return rating;
    }

    public int getDuration() {
        return duration;
    }

    public List<Genre> getGenres() {
        return genres;
    }
}
