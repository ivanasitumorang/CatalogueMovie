package azuka.com.cataloguemovie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import azuka.com.cataloguemovie.R;
import azuka.com.cataloguemovie.fragments.FavoriteFragment;
import azuka.com.cataloguemovie.fragments.NowPlayingFragment;
import azuka.com.cataloguemovie.fragments.SearchMovieFragment;
import azuka.com.cataloguemovie.fragments.UpComingFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btm_nav)
    BottomNavigationView bottomNavigationView;
    private ActionBar toolbar;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_now_playing:
                    Fragment fragment = new NowPlayingFragment();
                    toolbar.setTitle(getString(R.string.now_playing));
                    loadFragment(fragment);
                    return true;

                case R.id.nav_up_coming:
                    fragment = new UpComingFragment();
                    toolbar.setTitle(getString(R.string.up_coming));
                    loadFragment(fragment);
                    return true;

                case R.id.nav_search:
                    fragment = new SearchMovieFragment();
                    toolbar.setTitle(getString(R.string.search_movie));
                    loadFragment(fragment);
                    return true;

                case R.id.nav_fav:
                    fragment = new FavoriteFragment();
                    toolbar.setTitle(getString(R.string.favorite));
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        loadFragment(new NowPlayingFragment());
    }

    private void initView() {
        if (getSupportActionBar() != null) {
            toolbar = getSupportActionBar();
            bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
            toolbar.setTitle(R.string.now_playing);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.act_change_language) {
            invalidateOptionsMenu();
            startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
        }
        return super.onOptionsItemSelected(item);
    }
}
