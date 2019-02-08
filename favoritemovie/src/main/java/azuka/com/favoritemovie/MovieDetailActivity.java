package azuka.com.favoritemovie;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import azuka.com.favoritemovie.constants.Strings;
import azuka.com.favoritemovie.models.Movie;
import butterknife.BindView;
import butterknife.ButterKnife;

import static azuka.com.favoritemovie.database.DatabaseContract.CONTENT_URI;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.IS_FAVORITE;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.FAV_MOVIE_ID;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.ORIGINAL_LANGUAGE;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.ORIGINAL_TITLE;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.OVERVIEW;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.POSTER_PATH;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.RELEASE_DATE;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.RUNTIME;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.TAGLINE;
import static azuka.com.favoritemovie.database.DatabaseContract.FavoriteMovieColumns.VOTE_AVERAGE;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_poster)
    ImageView ivPoster;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_tagline)
    TextView tvTagline;
    @BindView(R.id.tv_rating)
    TextView tvRating;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.tv_language)
    TextView tvLanguage;
    @BindView(R.id.tv_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.tv_overview)
    TextView tvOverview;

    private ActionBar toolbar;
    private Movie movie;
    private Boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        setInit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDetailMovie();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isFavorite) {
            menu.findItem(R.id.btn_fav).setIcon(R.drawable.ic_heart_filled);
        } else {
            menu.findItem(R.id.btn_fav).setIcon(R.drawable.ic_heart_outline);
        }
        menu.findItem(R.id.btn_fav).getIcon().setTint(getResources().getColor(R.color.colorSecondary));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_fav) {
            invalidateOptionsMenu();
            toggleFavorite(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setInit() {
        if (getSupportActionBar() != null) {
            toolbar = getSupportActionBar();
            toolbar.setTitle(R.string.detail_movie);
            toolbar.setDisplayHomeAsUpEnabled(true);
        }
        isFavorite = true;
    }

    private void toggleFavorite(MenuItem item) {
        if (isFavorite) {
            removeFromFavorite();
            showToast(getString(R.string.hint_removed_from_favorite));
            isFavorite = false;
        } else {
            saveToFavorite();
            showToast(getString(R.string.hint_added_to_favorite));
            item.setIcon(R.drawable.ic_heart_outline);
            isFavorite = true;
        }
    }

    private void loadDetailMovie() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) movie = new Movie(cursor);
                cursor.close();
                setView(movie);
            }
        }
    }

    private void setView(Movie movie) {
        Glide.with(this)
                .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.no_picture).error(R.drawable.no_picture))
                .load(Strings.POSTER_BIG + movie.getPosterPath())
                .into(ivPoster);
        tvTitle.setText(movie.getTitle());
        tvTagline.setText(movie.getTagline());
        tvRating.setText(String.valueOf(movie.getRating()));
        tvDuration.setText(String.valueOf(movie.getDuration()));
        tvLanguage.setText(movie.getLanguage().toUpperCase());
        tvReleaseDate.setText(movie.getReleaseDate());
        tvOverview.setText(movie.getOverview());
        toolbar.setTitle(movie.getTitle());
    }

    private void saveToFavorite() {
        ContentValues values = new ContentValues();
        values.put(FAV_MOVIE_ID, movie.getMovieId());
        values.put(POSTER_PATH, movie.getPosterPath());
        values.put(ORIGINAL_TITLE, movie.getTitle());
        values.put(OVERVIEW, movie.getOverview());
        values.put(RELEASE_DATE, movie.getReleaseDate());
        values.put(ORIGINAL_LANGUAGE, movie.getLanguage());
        values.put(TAGLINE, movie.getTagline());
        values.put(VOTE_AVERAGE, movie.getRating());
        values.put(RUNTIME, movie.getDuration());
        values.put(IS_FAVORITE, Strings.YES);
        getContentResolver().insert(CONTENT_URI, values);
    }

    private void removeFromFavorite() {
        getContentResolver().delete(getIntent().getData(), null, null);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
