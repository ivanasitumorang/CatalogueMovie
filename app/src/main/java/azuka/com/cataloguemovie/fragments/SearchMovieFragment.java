package azuka.com.cataloguemovie.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import azuka.com.cataloguemovie.BuildConfig;
import azuka.com.cataloguemovie.R;
import azuka.com.cataloguemovie.activities.MovieDetailActivity;
import azuka.com.cataloguemovie.adapters.MoviesAdapter;
import azuka.com.cataloguemovie.constants.Strings;
import azuka.com.cataloguemovie.listeners.RecyclerViewClickListener;
import azuka.com.cataloguemovie.models.ApiResponse;
import azuka.com.cataloguemovie.models.Movie;
import azuka.com.cataloguemovie.services.ApiService;
import azuka.com.cataloguemovie.services.ApiUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMovieFragment extends Fragment implements View.OnClickListener, RecyclerViewClickListener {

    @BindView(R.id.pb_loading)
    ProgressBar progressBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_search)
    Button btnSearch;
    private ApiService apiService;
    private List<Movie> movieList;
    private MoviesAdapter moviesAdapter;

    public SearchMovieFragment() {
    }

    public static SearchMovieFragment newInstance() {
        return (SearchMovieFragment) new SearchMovieFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        apiService = ApiUtils.getMovieApi();
        View view = inflater.inflate(R.layout.fragment_search_movie, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setInit();
    }

    private void setInit() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        moviesAdapter = new MoviesAdapter(getContext(), this);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchMovie(v.getText().toString());
                }
                return false;
            }
        });
        btnSearch.setOnClickListener(this);
    }

    private void searchMovie(String title) {
        progressBar.setVisibility(View.VISIBLE);
        apiService.search(BuildConfig.TMDB_API_KEY, Strings.LANGUAGE, title).enqueue(new Callback<ApiResponse<List<Movie>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Movie>>> call, Response<ApiResponse<List<Movie>>> response) {
                if (response.isSuccessful()) {
                    movieList = response.body().getResults();
                    moviesAdapter.setMovies(movieList);
                    recyclerView.setAdapter(moviesAdapter);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Movie>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.w("onFailure", t.getMessage());
                Toast.makeText(getContext(), getString(R.string.hint_no_internet), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                String query = etSearch.getText().toString();
                if (query.isEmpty()) {
                    etSearch.setError(getString(R.string.hint_fill_form));
                } else {
                    searchMovie(query);
                }
                break;
        }
    }

    @Override
    public void onItemClickListener(Movie movie) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra(Strings.MOVIE_ID, movie.getMovieId());
        startActivity(intent);
    }
}
