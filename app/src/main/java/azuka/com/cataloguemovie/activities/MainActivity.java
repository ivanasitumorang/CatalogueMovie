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

public class MainActivity extends AppCompatActivity {

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onStart() {
        loadFragment(new NowPlayingFragment());
        super.onStart();
    }

    private void initView(){
        if (getSupportActionBar()!=null){
            toolbar = getSupportActionBar();
            BottomNavigationView bottomNavigationView = findViewById(R.id.btm_nav);
            bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
            toolbar.setTitle(R.string.now_playing);
        }
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()){
                case R.id.nav_now_playing:
                    toolbar.setTitle(R.string.now_playing);
                    fragment = new NowPlayingFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.nav_up_coming:
                    toolbar.setTitle(R.string.up_coming);
                    fragment = new UpComingFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.nav_search:
                    toolbar.setTitle(R.string.search_movie);
                    fragment = new SearchMovieFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.nav_fav:
                    toolbar.setTitle(R.string.favorite);
                    fragment = new FavoriteFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.act_change_language){
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
