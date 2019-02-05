package azuka.com.cataloguemovie.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import azuka.com.cataloguemovie.R;
import azuka.com.cataloguemovie.activities.MovieDetailActivity;
import azuka.com.cataloguemovie.constants.Strings;

import static azuka.com.cataloguemovie.database.DatabaseContract.CONTENT_URI;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteMovieWidget extends AppWidgetProvider {

    private static final String CLICK_ACTION = "azuka.com.cataloguemovie.CLICK_ACTION";
    public static final String EXTRA_ITEM = "EXTRA_ITEM";
    public static final String UPDATE_ACTION = "azuka.com.cataloguemovie.UPDATE_ACTION";

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_movie_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.tv_no_data);

        Intent clickIntent = new Intent(context, FavoriteMovieWidget.class);
        clickIntent.setAction(FavoriteMovieWidget.CLICK_ACTION);
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, clickPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals(CLICK_ACTION)) {
                Intent i = new Intent(context, MovieDetailActivity.class);
                Uri uri = Uri.parse(CONTENT_URI + "/" + intent.getStringExtra(Strings.MOVIE_ID));
                i.setData(uri);
                context.startActivity(i);
            }
            if (intent.getAction().equals(UPDATE_ACTION)){
                int widgetIDs[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, FavoriteMovieWidget.class));
                AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(widgetIDs, R.id.stack_view);
            }
        }
        super.onReceive(context, intent);
    }
}

