package azuka.com.cataloguemovie.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

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

public class NowPlayingFragment extends Fragment implements RecyclerViewClickListener {

    @BindView(R.id.pb_loading)
    ProgressBar progressBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private ApiService apiService;
    private ArrayList<Movie> movieList;
    private MoviesAdapter moviesAdapter;

    public NowPlayingFragment() {
    }

    public static NowPlayingFragment newInstance() {

        Bundle args = new Bundle();

        NowPlayingFragment fragment = new NowPlayingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        apiService = ApiUtils.getMovieApi();
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        super.onViewCreated(view, savedInstanceState);
        setInit();
        loadMovies();
    }

    private void setInit() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        moviesAdapter = new MoviesAdapter(getContext(), this);
    }

    private void loadMovies() {
        progressBar.setVisibility(View.VISIBLE);
        apiService.getNowPlaying(BuildConfig.TMDB_API_KEY, Strings.LANGUAGE).enqueue(new Callback<ApiResponse<ArrayList<Movie>>>() {
            @Override
            public void onResponse(Call<ApiResponse<ArrayList<Movie>>> call, Response<ApiResponse<ArrayList<Movie>>> response) {
                if (response.isSuccessful()) {
                    movieList = response.body().getResults();
                    moviesAdapter.setMovies(movieList);
                    recyclerView.setAdapter(moviesAdapter);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ApiResponse<ArrayList<Movie>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.w("onFailure", t.getMessage());
                showToast(getString(R.string.hint_no_internet));
            }
        });
    }

    @Override
    public void onItemClickListener(Movie movie) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra(Strings.MOVIE_ID, movie.getMovieId());
        startActivity(intent);
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
