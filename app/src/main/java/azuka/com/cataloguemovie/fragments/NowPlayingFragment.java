package azuka.com.cataloguemovie.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

public class NowPlayingFragment extends Fragment implements RecyclerViewClickListener {

    @BindView(R.id.pb_loading)
    ProgressBar progressBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private ApiService apiService;
    private List<Movie> movieList;
    private MoviesAdapter moviesAdapter;
    private Parcelable recyclerLayout;

    public NowPlayingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);

    }

    private void restoreRecyclerLayoutPosition(){
        if (recyclerLayout != null){
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerLayout);
        }
    }

    public static NowPlayingFragment newInstance() {
        return (NowPlayingFragment) new NowPlayingFragment();
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
        if (savedInstanceState != null){
            movieList = savedInstanceState.getParcelableArrayList(Strings.MOVIE_LIST);
            recyclerLayout = savedInstanceState.getParcelable(Strings.RECYCLER_LAYOUT);
            moviesAdapter.setMovies(movieList);
            restoreRecyclerLayoutPosition();
            moviesAdapter.notifyDataSetChanged();
        } else {
            loadMovies();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(Strings.MOVIE_LIST, (ArrayList<? extends Parcelable>) movieList);
        outState.putParcelable(Strings.RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        movieList = savedInstanceState.getParcelableArrayList(Strings.MOVIE_LIST);
        recyclerLayout = savedInstanceState.getParcelable(Strings.RECYCLER_LAYOUT);
    }

    private void setInit() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        moviesAdapter = new MoviesAdapter(getContext(), this);
    }

    private void loadMovies() {
        progressBar.setVisibility(View.VISIBLE);
        apiService.getNowPlaying(BuildConfig.TMDB_API_KEY, Strings.LANGUAGE).enqueue(new Callback<ApiResponse<List<Movie>>>() {
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
    public void onItemClickListener(Movie movie) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra(Strings.MOVIE_ID, movie.getMovieId());
        startActivity(intent);
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
