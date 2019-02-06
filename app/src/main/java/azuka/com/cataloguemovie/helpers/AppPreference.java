package azuka.com.cataloguemovie.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import azuka.com.cataloguemovie.constants.Strings;

/**
 * Created by Ivana Situmorang on 2/5/2019.
 */
public class AppPreference {
    SharedPreferences prefs;
    Context context;

    public AppPreference(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public void setDailyReminder(Boolean input, String time){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Strings.PREFERENCES_REMINDER_DAILY, input);
        editor.putString(Strings.PREFERENCES_REMINDER_DAILY_TIME, time);
        editor.apply();
    }

    public Boolean getDailyReminder(){
        return prefs.getBoolean(Strings.PREFERENCES_REMINDER_DAILY, true);
    }

    public void setReleaseTodayReminder(Boolean input, String time){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Strings.PREFERENCES_REMINDER_RELEASE, input);
        editor.putString(Strings.PREFERENCES_REMINDER_DAILY_TIME, time);
        editor.apply();
    }

    public Boolean getReleaseTodayReminder(){
        return prefs.getBoolean(Strings.PREFERENCES_REMINDER_RELEASE, true);
    }
}
