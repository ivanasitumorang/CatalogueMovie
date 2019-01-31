package azuka.com.cataloguemovie.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import azuka.com.cataloguemovie.constants.Strings;

import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.IS_FAVORITE;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.MOVIE_ID;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.ORIGINAL_LANGUAGE;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.ORIGINAL_TITLE;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.OVERVIEW;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.POSTER_PATH;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.RELEASE_DATE;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.RUNTIME;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.TAGLINE;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.VOTE_AVERAGE;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.getColumnFloat;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.getColumnInt;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.getColumnString;

/**
 * Created by Ivana Situmorang on 1/24/2019.
 */
public class Movie implements Parcelable {

    @SerializedName("id")
    @Expose
    private String movieId;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("original_title")
    @Expose
    private String title;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("original_language")
    @Expose
    private String language;

    @SerializedName("tagline")
    @Expose
    private String tagline;

    private String isFavorite;

    public String getIsFavorite() {
        if (isFavorite == null) {
            isFavorite = Strings.NO;
        }
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @SerializedName("vote_average")
    @Expose
    private float rating;

    @SerializedName("runtime")
    @Expose
    private int duration;

    @SerializedName("genres")
    @Expose
    private List<Genre> genres;

    public String getMovieId() {
        return movieId;
    }

    public String getPosterPath() {
        return posterPath;
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

    public Movie() {
    }

    public Movie(Cursor cursor) {
        this.movieId = getColumnString(cursor, MOVIE_ID);
        this.posterPath = getColumnString(cursor, POSTER_PATH);
        this.title = getColumnString(cursor, ORIGINAL_TITLE);
        this.overview = getColumnString(cursor, OVERVIEW);
        this.releaseDate = getColumnString(cursor, RELEASE_DATE);
        this.language = getColumnString(cursor, ORIGINAL_LANGUAGE);
        this.tagline = getColumnString(cursor, TAGLINE);
        this.rating = getColumnFloat(cursor, VOTE_AVERAGE);
        this.duration = getColumnInt(cursor, RUNTIME);
        this.isFavorite = getColumnString(cursor, IS_FAVORITE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.movieId);
        dest.writeString(this.posterPath);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.language);
        dest.writeString(this.tagline);
        dest.writeString(this.isFavorite);
        dest.writeFloat(this.rating);
        dest.writeInt(this.duration);
        dest.writeList(this.genres);
    }

    protected Movie(Parcel in) {
        this.movieId = in.readString();
        this.posterPath = in.readString();
        this.title = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.language = in.readString();
        this.tagline = in.readString();
        this.isFavorite = in.readString();
        this.rating = in.readFloat();
        this.duration = in.readInt();
        this.genres = new ArrayList<Genre>();
        in.readList(this.genres, Genre.class.getClassLoader());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
