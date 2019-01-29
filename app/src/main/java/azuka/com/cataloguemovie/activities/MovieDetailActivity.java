package azuka.com.cataloguemovie.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import azuka.com.cataloguemovie.BuildConfig;
import azuka.com.cataloguemovie.R;
import azuka.com.cataloguemovie.adapters.GenreAdapter;
import azuka.com.cataloguemovie.constants.Strings;
import azuka.com.cataloguemovie.models.Genre;
import azuka.com.cataloguemovie.models.Movie;
import azuka.com.cataloguemovie.services.ApiService;
import azuka.com.cataloguemovie.services.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView ivPoster;
    private TextView tvTitle, tvTagline, tvRating, tvDuration, tvLanguage, tvReleaseDate, tvOverview;
    private ApiService apiService;
    private List<Genre> genres;
    private ProgressBar progressBar;
    private ActionBar toolbar;
    private GenreAdapter genreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        apiService = ApiUtils.getMovieApi();
        setInit();
        loadMovieDetail(getIntent().getStringExtra(Strings.MOVIE_ID));
    }

    private void setInit(){
        if (getSupportActionBar() != null){
            toolbar = getSupportActionBar();
            toolbar.setTitle(R.string.detail_movie);
            toolbar.setDisplayHomeAsUpEnabled(true);
        }
        ivPoster = findViewById(R.id.iv_poster);
        tvTitle = findViewById(R.id.tv_title);
        tvTagline = findViewById(R.id.tv_tagline);
        tvRating = findViewById(R.id.tv_rating);
        tvDuration = findViewById(R.id.tv_duration);
        tvLanguage = findViewById(R.id.tv_language);
        tvReleaseDate = findViewById(R.id.tv_release_date);
        tvOverview = findViewById(R.id.tv_overview);
        progressBar = findViewById(R.id.progress_bar);
        RecyclerView rvGenre = findViewById(R.id.rv_genre);

        genreAdapter = new GenreAdapter(getApplicationContext());
        rvGenre.setAdapter(genreAdapter);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvGenre.setLayoutManager(layoutManager);
        //rvGenre.setHasFixedSize(true);
        rvGenre.setNestedScrollingEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void loadMovieDetail(String movieId){
        progressBar.setVisibility(View.VISIBLE);
        apiService.getMovieDetail(movieId, BuildConfig.TMDB_API_KEY, Strings.LANGUAGE).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Movie movie = response.body();
                    genres = response.body().getGenres();
                    setView(movie);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

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
        genreAdapter.setGenre(genres);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_fav){
            toogleFavorite(item);
        }
        return super.onOptionsItemSelected(item);
    }



    private void toogleFavorite(MenuItem item){
        if (item.getIcon().getConstantState().equals(getDrawable(R.drawable.ic_heart_outline).getConstantState())){
            item.setIcon(getDrawable(R.drawable.ic_heart_filled));
            showToast(getString(R.string.hint_added_to_favorite));
        } else {
            item.setIcon(getDrawable(R.drawable.ic_heart_outline));
            showToast(getString(R.string.hint_removed_from_favorite));
        }
    }

    private void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
