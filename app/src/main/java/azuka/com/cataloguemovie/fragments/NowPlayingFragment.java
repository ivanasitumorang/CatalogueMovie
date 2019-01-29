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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NowPlayingFragment extends Fragment implements RecyclerViewClickListener {
    private ApiService apiService;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<Movie> movieList;
    private MoviesAdapter moviesAdapter;

    public NowPlayingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        apiService = ApiUtils.getMovieApi();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_now_playing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView(view);
        moviesAdapter = new MoviesAdapter(getContext(), this);
        loadMovies();
    }

    private void setView(View view){
        progressBar = view.findViewById(R.id.pb_loading);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }

    private void loadMovies(){
        progressBar.setVisibility(View.VISIBLE);
        apiService.getNowPlaying(BuildConfig.TMDB_API_KEY, Strings.LANGUAGE).enqueue(new Callback<ApiResponse<List<Movie>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Movie>>> call, Response<ApiResponse<List<Movie>>> response) {
                if (response.isSuccessful()){
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
}
