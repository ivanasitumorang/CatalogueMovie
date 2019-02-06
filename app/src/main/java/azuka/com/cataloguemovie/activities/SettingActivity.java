package azuka.com.cataloguemovie.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import azuka.com.cataloguemovie.R;
import azuka.com.cataloguemovie.helpers.AppPreference;
import azuka.com.cataloguemovie.reminder.DailyReminder;
import butterknife.BindView;
import butterknife.ButterKnife;

import static azuka.com.cataloguemovie.constants.Strings.TIME;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.tb_daily_reminder) ToggleButton tbDailyReminder;
    private DailyReminder dailyReminder;
    private AppPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dailyReminder = new DailyReminder();
        appPreference = new AppPreference(this);

        tbDailyReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appPreference.setDailyReminder(isChecked, TIME);
                if (isChecked){
                    dailyReminder.startReminder(SettingActivity.this, TIME, getString(R.string.daily_reminder_msg));
                } else {
                    dailyReminder.stopReminder(SettingActivity.this);
                }
            }
        });
        loadPreference();
    }

    private void loadPreference() {
        tbDailyReminder.setChecked(appPreference.getDailyReminder());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
