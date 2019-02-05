package azuka.com.cataloguemovie.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import azuka.com.cataloguemovie.R;
import azuka.com.cataloguemovie.constants.Strings;
import azuka.com.cataloguemovie.database.DatabaseContract;
import azuka.com.cataloguemovie.helpers.DatabaseHelper;
import azuka.com.cataloguemovie.models.Movie;

/**
 * Created by Ivana Situmorang on 2/3/2019.
 */
class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private final ArrayList<Movie> movieList = new ArrayList<>();
    private int appWidgetId;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        movieList.clear();
        loadWidgetData();
    }

    private void loadWidgetData () {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor = database.query(DatabaseContract.TABLE_FAV_MOVIE, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie fav = new Movie(cursor);
                movieList.add(fav);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.item_widget);

        try {
            Movie favoriteMovie = movieList.get(position);

            Bundle bundle = new Bundle();
            bundle.putString(Strings.MOVIE_ID, favoriteMovie.getMovieId());
            bundle.putString(Strings.MOVIE_TITLE, favoriteMovie.getTitle());

            Intent intent = new Intent();
            intent.putExtras(bundle);
            rv.setOnClickFillInIntent(R.id.iv_banner, intent);

            String posterUrl = Strings.POSTER_THUMB.replace("/w92/", "/w500/") + favoriteMovie.getPosterPath();
            try {
                Bitmap preview = Glide.with(context)
                        .asBitmap()
                        .load(posterUrl)
                        .apply(new RequestOptions().fitCenter())
                        .submit()
                        .get();
                rv.setImageViewBitmap(R.id.iv_banner, preview);
                rv.setTextViewText(R.id.tv_movie_title, favoriteMovie.getTitle());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
