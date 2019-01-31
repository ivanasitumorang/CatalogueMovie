package azuka.com.cataloguemovie.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import azuka.com.cataloguemovie.database.DatabaseContract;

/**
 * Created by Ivana Situmorang on 1/27/2019.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_MOVIE = String.format(
            "CREATE TABLE %s" +
                    " (%s TEXT PRIMARY KEY, " +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s FLOAT NOT NULL," +
                    " %s INTEGER NOT NULL," +
                    " %s TEXT DEFAULT 'N')",
            DatabaseContract.TABLE_FAV_MOVIE,
            DatabaseContract.FavoriteMovieColumns.MOVIE_ID,
            DatabaseContract.FavoriteMovieColumns.POSTER_PATH,
            DatabaseContract.FavoriteMovieColumns.ORIGINAL_TITLE,
            DatabaseContract.FavoriteMovieColumns.OVERVIEW,
            DatabaseContract.FavoriteMovieColumns.RELEASE_DATE,
            DatabaseContract.FavoriteMovieColumns.ORIGINAL_LANGUAGE,
            DatabaseContract.FavoriteMovieColumns.TAGLINE,
            DatabaseContract.FavoriteMovieColumns.VOTE_AVERAGE,
            DatabaseContract.FavoriteMovieColumns.RUNTIME,
            DatabaseContract.FavoriteMovieColumns.IS_FAVORITE
    );
    public static String DATABASE_NAME = "movie_catalogue";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_FAV_MOVIE);
        onCreate(db);
    }
}
