package azuka.com.cataloguemovie.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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

import azuka.com.cataloguemovie.R;
import azuka.com.cataloguemovie.activities.MovieDetailActivity;
import azuka.com.cataloguemovie.adapters.FavoriteAdapter;
import azuka.com.cataloguemovie.listeners.RecyclerViewClickListener;
import azuka.com.cataloguemovie.models.Movie;
import butterknife.BindView;
import butterknife.ButterKnife;

import static azuka.com.cataloguemovie.database.DatabaseContract.CONTENT_URI;

public class FavoriteFragment extends Fragment implements RecyclerViewClickListener {

    @BindView(R.id.pb_loading)
    ProgressBar progressBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;

    public FavoriteFragment() {
    }

    public static FavoriteFragment newInstance() {
        return (FavoriteFragment) new FavoriteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
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
        favoriteAdapter = new FavoriteAdapter(getContext(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMovies();
    }

    @Override
    public void onItemClickListener(Movie movie) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        Uri uri = Uri.parse(CONTENT_URI + "/" + movie.getMovieId());
        intent.setData(uri);
        startActivity(intent);
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void loadMovies() {
        progressBar.setVisibility(View.VISIBLE);
        new LoadFavoriteAsync().execute();
    }

    private class LoadFavoriteAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor movie) {
            super.onPostExecute(movie);
            progressBar.setVisibility(View.GONE);

            Cursor list = movie;
            favoriteAdapter.setMovies(list);
            favoriteAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(favoriteAdapter);

            int count = 0;
            try {
                count = ((list.getCount() > 0) ? list.getCount() : 0);
            } catch (Exception e) {
                Log.w("ERROR", e.getMessage());
            }
            if (count == 0) {
                showToast(getString(R.string.hint_no_favorite));
            }
        }
    }

}
