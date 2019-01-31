package azuka.com.cataloguemovie.activities;

import android.content.ContentValues;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static azuka.com.cataloguemovie.helpers.DatabaseContract.CONTENT_URI;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.IS_FAVORITE;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.MOVIE_ID;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.ORIGINAL_LANGUAGE;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.ORIGINAL_TITLE;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.OVERVIEW;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.POSTER_PATH;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.RELEASE_DATE;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.RUNTIME;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.TAGLINE;
import static azuka.com.cataloguemovie.helpers.DatabaseContract.MovieColumns.VOTE_AVERAGE;


public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_poster) ImageView ivPoster;
    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.tv_tagline) TextView tvTagline;
    @BindView(R.id.tv_rating) TextView tvRating;
    @BindView(R.id.tv_duration) TextView tvDuration;
    @BindView(R.id.tv_language) TextView tvLanguage;
    @BindView(R.id.tv_release_date) TextView tvReleaseDate;
    @BindView(R.id.tv_overview) TextView tvOverview;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

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
        loadDetailMovie();
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
        isFavorite = checkFavorite();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (helper != null) {
            helper.close();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*
        if (isFavorite){
            saveToFavorite();
        } else {
            removeFromFavorite();
        }
         */
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
        if (isFavorite){
            menu.findItem(R.id.btn_fav).setIcon(R.drawable.ic_heart_filled);
        } else {
            menu.findItem(R.id.btn_fav).setIcon(R.drawable.ic_heart_outline);
        }
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

    private void toggleFavorite(MenuItem item){
        if (isFavorite){
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

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private boolean checkFavorite(){
        uriDetailMovie = Uri.parse(CONTENT_URI + "/" + movieId);
        Cursor cursor = getContentResolver().query(uriDetailMovie, null, null, null, null);
        if (cursor != null){
            return cursor.getCount() > 0;
        }
        return false;
    }

    private void loadDetailMovie(){
        if (movieId != null){
            loadDataOnline();
        } else loadDataLocal();
    }

    private void loadDataLocal(){
        Uri uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null){
                if(cursor.moveToFirst()) movie = new Movie(cursor);
                cursor.close();
                setView(movie);
            }
        }
    }

    private void loadDataOnline() {
        progressBar.setVisibility(View.VISIBLE);
        apiService.getMovieDetail(movieId, BuildConfig.TMDB_API_KEY, Strings.LANGUAGE).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    movieFromServer = response.body();
                    setView(movieFromServer);
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
        Glide.with(getApplicationContext())
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

    private void saveToFavorite(){
        ContentValues values = new ContentValues();
        values.put(MOVIE_ID, movieFromServer.getMovieId());
        values.put(POSTER_PATH, movieFromServer.getPosterPath());
        values.put(ORIGINAL_TITLE, movieFromServer.getTitle());
        values.put(OVERVIEW, movieFromServer.getOverview());
        values.put(RELEASE_DATE, movieFromServer.getReleaseDate());
        values.put(ORIGINAL_LANGUAGE, movieFromServer.getLanguage());
        values.put(TAGLINE, movieFromServer.getTagline());
        values.put(VOTE_AVERAGE, movieFromServer.getRating());
        values.put(RUNTIME, movieFromServer.getDuration());
        values.put(IS_FAVORITE, Strings.YES);
        getContentResolver().insert(CONTENT_URI, values);
        /*
        if (loadDataLocal() != null){
            values.put(MOVIE_ID, loadDataLocal().getMovieId());
            values.put(POSTER_PATH, loadDataLocal().getPosterPath());
            values.put(ORIGINAL_TITLE, loadDataLocal().getTitle());
            values.put(OVERVIEW, loadDataLocal().getOverview());
            values.put(RELEASE_DATE, loadDataLocal().getReleaseDate());
            values.put(ORIGINAL_LANGUAGE, loadDataLocal().getLanguage());
            values.put(TAGLINE, loadDataLocal().getTagline());
            values.put(VOTE_AVERAGE, loadDataLocal().getRating());
            values.put(RUNTIME, loadDataLocal().getDuration());
            values.put(IS_FAVORITE, Strings.YES);
            getContentResolver().insert(CONTENT_URI, values);
        } else {
            values.put(MOVIE_ID, movieFromServer.getMovieId());
            values.put(POSTER_PATH, movieFromServer.getPosterPath());
            values.put(ORIGINAL_TITLE, movieFromServer.getTitle());
            values.put(OVERVIEW, movieFromServer.getOverview());
            values.put(RELEASE_DATE, movieFromServer.getReleaseDate());
            values.put(ORIGINAL_LANGUAGE, movieFromServer.getLanguage());
            values.put(TAGLINE, movieFromServer.getTagline());
            values.put(VOTE_AVERAGE, movieFromServer.getRating());
            values.put(RUNTIME, movieFromServer.getDuration());
            values.put(IS_FAVORITE, Strings.YES);
        }
         */
    }

    private void removeFromFavorite(){
        if (getIntent().getData() != null){
            getContentResolver().delete(getIntent().getData(), null, null);
        } else {
            getContentResolver().delete(uriDetailMovie, null, null);
        }

    }

}
