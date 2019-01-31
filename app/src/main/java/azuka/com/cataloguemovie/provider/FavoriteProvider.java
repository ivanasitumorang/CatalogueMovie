package azuka.com.cataloguemovie.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import azuka.com.cataloguemovie.helpers.FavoriteMovieHelper;

import static azuka.com.cataloguemovie.database.DatabaseContract.AUTHORITY;
import static azuka.com.cataloguemovie.database.DatabaseContract.CONTENT_URI;
import static azuka.com.cataloguemovie.database.DatabaseContract.TABLE_FAV_MOVIE;

/**
 * Created by Ivana Situmorang on 1/29/2019.
 */
public class FavoriteProvider extends ContentProvider {

    private static final int MOVIE = 1;
    private static final int ID = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, TABLE_FAV_MOVIE, MOVIE);
        uriMatcher.addURI(AUTHORITY, TABLE_FAV_MOVIE + "/#", ID);
    }

    private FavoriteMovieHelper favoriteMovieHelper;

    @Override
    public boolean onCreate() {
        favoriteMovieHelper = new FavoriteMovieHelper(getContext());
        favoriteMovieHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case MOVIE:
                cursor = favoriteMovieHelper.queryProvider();
                break;
            case ID:
                cursor = favoriteMovieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long added;

        switch (uriMatcher.match(uri)) {
            case MOVIE:
                added = favoriteMovieHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted;
        switch (uriMatcher.match(uri)) {
            case ID:
                deleted = favoriteMovieHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (deleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int updated;
        switch (uriMatcher.match(uri)) {
            case ID:
                updated = favoriteMovieHelper.updateProvider(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }

        if (updated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }
}
