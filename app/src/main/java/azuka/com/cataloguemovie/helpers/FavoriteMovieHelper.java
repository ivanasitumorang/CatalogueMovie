package azuka.com.cataloguemovie.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import azuka.com.cataloguemovie.models.Movie;

import static android.provider.BaseColumns._ID;
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
import static azuka.com.cataloguemovie.helpers.DatabaseContract.TABLE_FAV_MOVIE;

/**
 * Created by Ivana Situmorang on 1/29/2019.
 */
public class FavoriteMovieHelper {
    private static final String TABLE_NAME = TABLE_FAV_MOVIE;
    private Context context;
    private DatabaseHelper helper;
    private SQLiteDatabase database;

    public FavoriteMovieHelper(Context context) {
        this.context = context;
    }

    public FavoriteMovieHelper open() throws SQLException {
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
        return this;
    }

    public void close() {
        helper.close();
    }

    public ArrayList<Movie> query() {
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC",
                null);
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setMovieId(cursor.getString(cursor.getColumnIndexOrThrow(MOVIE_ID)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                movie.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_LANGUAGE)));
                movie.setTagline(cursor.getString(cursor.getColumnIndexOrThrow(TAGLINE)));
                movie.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow(VOTE_AVERAGE)));
                movie.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(RUNTIME)));
                movie.setIsFavorite(cursor.getString(cursor.getColumnIndexOrThrow(IS_FAVORITE)));

                arrayList.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(Movie movie){
        ContentValues values =  new ContentValues();
        values.put(MOVIE_ID, movie.getMovieId());
        values.put(POSTER_PATH, movie.getPosterPath());
        values.put(ORIGINAL_TITLE, movie.getTitle());
        values.put(OVERVIEW, movie.getOverview());
        values.put(RELEASE_DATE, movie.getReleaseDate());
        values.put(ORIGINAL_LANGUAGE, movie.getLanguage());
        values.put(TAGLINE, movie.getTagline());
        values.put(VOTE_AVERAGE, movie.getRating());
        values.put(RUNTIME, movie.getDuration());
        values.put(IS_FAVORITE, 1);

        return database.insert(TABLE_NAME, null, values);
    }

    public int update(Movie movie){
        ContentValues values =  new ContentValues();
        values.put(MOVIE_ID, movie.getMovieId());
        values.put(POSTER_PATH, movie.getPosterPath());
        values.put(ORIGINAL_TITLE, movie.getTitle());
        values.put(OVERVIEW, movie.getOverview());
        values.put(RELEASE_DATE, movie.getReleaseDate());
        values.put(ORIGINAL_LANGUAGE, movie.getLanguage());
        values.put(TAGLINE, movie.getTagline());
        values.put(VOTE_AVERAGE, movie.getRating());
        values.put(RUNTIME, movie.getDuration());
        values.put(IS_FAVORITE, 1);
        return database.update(TABLE_NAME, values, MOVIE_ID + "= '" + movie.getMovieId() + "'", null);
    }

    public int delete(int movieId){
        return database.delete(TABLE_NAME, MOVIE_ID + " = '"+movieId+"'", null);
    }

    public Cursor queryByIdProvider(String movieId){
        return database.query(TABLE_NAME,
                null,
                MOVIE_ID + " = ?",
                new String[]{movieId},
                null,
                null,
                null,
                null);
    }

    public Cursor queryProvider(){
        return database.query(TABLE_NAME,
                null,
                IS_FAVORITE + " = ?",
                new String[]{"Y"},
                null,
                null,
                MOVIE_ID + " DESC");
    }

    public long insertProvider(ContentValues values){
        return database.insert(TABLE_NAME,null, values);
    }

    public int updateProvider(String movieId, ContentValues values) {
        return database.update(TABLE_NAME, values, MOVIE_ID + " = ?", new String[]{movieId});
    }

    public int deleteProvider(String movieId){
        return database.delete(TABLE_NAME,MOVIE_ID + " = ?", new String[]{movieId});
    }
}
