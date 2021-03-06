package azuka.com.cataloguemovie.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ivana Situmorang on 1/29/2019.
 */
public class DatabaseContract {

    public static final String TABLE_FAV_MOVIE = "table_fav_movie";
    public static final String AUTHORITY = "azuka.com.cataloguemovie";
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_FAV_MOVIE)
            .build();

    private DatabaseContract() {
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static float getColumnFloat(Cursor cursor, String columnName) {
        return cursor.getFloat(cursor.getColumnIndex(columnName));
    }

    public static final class FavoriteMovieColumns implements BaseColumns {
        public static String FAV_MOVIE_ID = "fav_movie_id";
        public static String POSTER_PATH = "poster_path";
        public static String ORIGINAL_TITLE = "original_title";
        public static String OVERVIEW = "overview";
        public static String RELEASE_DATE = "release_date";
        public static String ORIGINAL_LANGUAGE = "original_language";
        public static String TAGLINE = "tagline";
        public static String VOTE_AVERAGE = "vote_average";
        public static String RUNTIME = "runtime";
        public static String IS_FAVORITE = "is_favorite";
    }

}
