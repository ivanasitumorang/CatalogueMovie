package azuka.com.cataloguemovie.reminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import azuka.com.cataloguemovie.BuildConfig;
import azuka.com.cataloguemovie.R;
import azuka.com.cataloguemovie.activities.MainActivity;
import azuka.com.cataloguemovie.activities.MovieDetailActivity;
import azuka.com.cataloguemovie.constants.Strings;
import azuka.com.cataloguemovie.models.ApiResponse;
import azuka.com.cataloguemovie.models.Movie;
import azuka.com.cataloguemovie.services.ApiService;
import azuka.com.cataloguemovie.services.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ivana Situmorang on 2/6/2019.
 */
public class ReleaseTodayReminder extends BroadcastReceiver {

    private final int REQUEST_CODE_RELEASE = 12;
    public static String CHANNEL_ID = "ch_1";
    public static CharSequence CHANNEL_NAME = "release_today";
    private ArrayList<Movie> movieList = new ArrayList<>(), tmpMovies;
    private NotificationCompat.Builder mBuilder;

    public ReleaseTodayReminder() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        getReleaseTodayMovies(context);
    }

    private void getReleaseTodayMovies(final Context context) {
        ApiService apiService = ApiUtils.getMovieApi();
        Date cal = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String dateToday = dateFormat.format(cal);
        apiService.getUpComing(BuildConfig.TMDB_API_KEY, Strings.LANGUAGE).enqueue(new Callback<ApiResponse<ArrayList<Movie>>>() {
            @Override
            public void onResponse(Call<ApiResponse<ArrayList<Movie>>> call, Response<ApiResponse<ArrayList<Movie>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResults() != null) {
                        if (response.body().getResults() != null) {
                            tmpMovies = response.body().getResults();
                            for (Movie movie : tmpMovies) {
                                String date = movie.getReleaseDate();
                                if (date.equalsIgnoreCase(dateToday)) {
                                    movieList.add(movie);
                                }
                            }
                        }
                        releaseTodayNotification(context);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ArrayList<Movie>>> call, Throwable t) {

            }
        });
    }

    private void releaseTodayNotification(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notifications_48dp);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            mBuilder.setChannelId(CHANNEL_ID);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }

        Intent intent;
        PendingIntent pendingIntent;

        int numMovies = 0;
        try {
            numMovies = ((movieList.size() > 0) ? movieList.size() : 0);
        } catch (Exception e) {
            Log.w("ERROR", e.getMessage());
        }

        String title = "";
        String msg = "";

        if (numMovies == 0) {
            title = "Today release";
            msg = "Tidak ada Movie rilis hari ini";
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE_RELEASE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setSmallIcon(R.drawable.ic_notifications_48dp)
                    .setLargeIcon(largeIcon)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSound(alarmSound)
                    .setAutoCancel(true);
            if (mNotificationManager != null) {
                mNotificationManager.notify(0, mBuilder.build());
            }
        } else {
            intent = new Intent(context, MovieDetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            for (int i = 0; i < numMovies; i++) {
                title = movieList.get(i).getTitle();
                msg = movieList.get(i).getTitle() + " " + context.getString(R.string.release_today_reminder_msg);
                intent.putExtra(Strings.MOVIE_ID, movieList.get(i).getMovieId());
                pendingIntent = PendingIntent.getActivity(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setSmallIcon(R.drawable.ic_notifications_48dp)
                        .setLargeIcon(largeIcon)
                        .setContentIntent(pendingIntent)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setSound(alarmSound)
                        .setAutoCancel(true);
                if (mNotificationManager != null) {
                    mNotificationManager.notify(i, mBuilder.build());
                }
            }
        }
    }

    public void startReminder(Context context, String time) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseTodayReminder.class);

        String timeArray[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_RELEASE, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void stopReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_RELEASE, intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

}
