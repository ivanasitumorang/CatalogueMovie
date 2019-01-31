package azuka.com.cataloguemovie.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ivana Situmorang on 1/27/2019.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "movie_catalogue";
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
            DatabaseContract.MovieColumns.MOVIE_ID,
            DatabaseContract.MovieColumns.POSTER_PATH,
            DatabaseContract.MovieColumns.ORIGINAL_TITLE,
            DatabaseContract.MovieColumns.OVERVIEW,
            DatabaseContract.MovieColumns.RELEASE_DATE,
            DatabaseContract.MovieColumns.ORIGINAL_LANGUAGE,
            DatabaseContract.MovieColumns.TAGLINE,
            DatabaseContract.MovieColumns.VOTE_AVERAGE,
            DatabaseContract.MovieColumns.RUNTIME,
            DatabaseContract.MovieColumns.IS_FAVORITE
    );

    public DatabaseHelper(Context context){
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
