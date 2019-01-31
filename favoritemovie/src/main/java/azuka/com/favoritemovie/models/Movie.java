package azuka.com.favoritemovie.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.IS_FAVORITE;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.MOVIE_ID;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.ORIGINAL_LANGUAGE;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.ORIGINAL_TITLE;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.OVERVIEW;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.POSTER_PATH;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.RELEASE_DATE;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.RUNTIME;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.TAGLINE;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.VOTE_AVERAGE;
import static azuka.com.favoritemovie.database.DatabaseContract.getColumnFloat;
import static azuka.com.favoritemovie.database.DatabaseContract.getColumnInt;
import static azuka.com.favoritemovie.database.DatabaseContract.getColumnString;

/**
 * Created by Ivana Situmorang on 1/31/2019.
 */
public class Movie implements Parcelable {
    private String movieId;
    private String posterPath;
    private String title;
    private String overview;
    private String releaseDate;
    private String language;
    private String tagline;
    private String isFavorite;
    private float rating;
    private int duration;

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
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
