package azuka.com.cataloguemovie.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import azuka.com.cataloguemovie.BuildConfig;
import azuka.com.cataloguemovie.R;
import azuka.com.cataloguemovie.constants.Strings;
import azuka.com.cataloguemovie.helpers.FavoriteMovieHelper;
import azuka.com.cataloguemovie.models.Movie;
import azuka.com.cataloguemovie.services.ApiService;
import azuka.com.cataloguemovie.services.ApiUtils;
import azuka.com.cataloguemovie.widget.FavoriteMovieWidget;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static azuka.com.cataloguemovie.database.DatabaseContract.CONTENT_URI;
import static azuka.com.cataloguemovie.database.DatabaseContract.FavoriteMovieColumns.FAV_MOVIE_ID;
import static azuka.com.cataloguemovie.database.DatabaseContract.FavoriteMovieColumns.IS_FAVORITE;
import static azuka.com.cataloguemovie.database.DatabaseContract.FavoriteMovieColumns.ORIGINAL_LANGUAGE;
import static azuka.com.cataloguemovie.database.DatabaseContract.FavoriteMovieColumns.ORIGINAL_TITLE;
import static azuka.com.cataloguemovie.database.DatabaseContract.FavoriteMovieColumns.OVERVIEW;
import static azuka.com.cataloguemovie.database.DatabaseContract.FavoriteMovieColumns.POSTER_PATH;
import static azuka.com.cataloguemovie.database.DatabaseContract.FavoriteMovieColumns.RELEASE_DATE;
import static azuka.com.cataloguemovie.database.DatabaseContract.FavoriteMovieColumns.RUNTIME;
import static azuka.com.cataloguemovie.database.DatabaseContract.FavoriteMovieColumns.TAGLINE;
import static azuka.com.cataloguemovie.database.DatabaseContract.FavoriteMovieColumns.VOTE_AVERAGE;


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
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ApiService apiService;
    private ActionBar toolbar;
    private FavoriteMovieHelper helper;
    private Movie movie, movieFromServer;
    private String movieId;
    private Boolean isFavorite = false;
    private Uri uriDetailMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        setInit();
        if (savedInstanceState == null) {
            loadDetailMovie();
            isFavorite = checkFavorite();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Strings.MOVIE, movie);
        outState.putBoolean(Strings.FAVORITED, isFavorite);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable(Strings.MOVIE);
            isFavorite = savedInstanceState.getBoolean(Strings.FAVORITED);
            setView(movie);
        }
    }

    private void setInit() {
        if (getSupportActionBar() != null) {
            toolbar = getSupportActionBar();
            toolbar.setTitle(R.string.detail_movie);
            toolbar.setDisplayHomeAsUpEnabled(true);
        }
        apiService = ApiUtils.getMovieApi();
        helper = new FavoriteMovieHelper(this);
        helper.open();
        movieId = getIntent().getStringExtra(Strings.MOVIE_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (helper != null) {
            helper.close();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
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

    private void toggleFavorite(MenuItem item) {
        if (isFavorite) {
            removeFromFavorite();
            showToast(getString(R.string.hint_removed_from_favorite));
            isFavorite = false;
        } else {
            saveToFavorite(movie);
            showToast(getString(R.string.hint_added_to_favorite));
            item.setIcon(R.drawable.ic_heart_outline);
            isFavorite = true;
        }
        Intent updateWidget = new Intent(this, FavoriteMovieWidget.class);
        updateWidget.setAction(FavoriteMovieWidget.UPDATE_ACTION);
        sendBroadcast(updateWidget);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private boolean checkFavorite() {
        if (!loadDataLocal()) {
            uriDetailMovie = Uri.parse(CONTENT_URI + "/" + movieId);
        } else {
            uriDetailMovie = Uri.parse(CONTENT_URI + "/" + movie.getMovieId());
        }

        Cursor cursor = getContentResolver().query(uriDetailMovie, null, null, null, null);
        if (cursor != null) {
            return cursor.getCount() > 0;
        }
        return false;
    }

    private void loadDetailMovie() {
        if (movieId != null) {
            loadDataOnline();
        } else loadDataLocal();
    }

    private boolean loadDataLocal() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                Movie temp = null;
                if (cursor.moveToFirst()) temp = new Movie(cursor);
                cursor.close();
                setView(temp);
                movie = temp;
                return true;
            }
        }
        return false;
    }

    private void loadDataOnline() {
        progressBar.setVisibility(View.VISIBLE);
        apiService.getMovieDetail(movieId, BuildConfig.TMDB_API_KEY, Strings.LANGUAGE).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    movieFromServer = response.body();
                    movie = movieFromServer;
                    setView(movie);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.w("onFailure", t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setView(Movie movie) {
        Glide.with(MovieDetailActivity.this)
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

    private void saveToFavorite(Movie movie) {
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
        if (getIntent().getData() != null) {
            getContentResolver().delete(getIntent().getData(), null, null);
        } else {
            getContentResolver().delete(uriDetailMovie, null, null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
