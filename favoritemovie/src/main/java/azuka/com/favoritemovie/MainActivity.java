package azuka.com.favoritemovie;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import azuka.com.favoritemovie.adapters.FavoriteMovieAdapter;
import azuka.com.favoritemovie.listeners.RecyclerViewClickListener;
import azuka.com.favoritemovie.models.Movie;
import butterknife.BindView;
import butterknife.ButterKnife;

import static azuka.com.favoritemovie.database.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private FavoriteMovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setInit();
        loadFavoriteMovies();
    }

    private void setInit() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new FavoriteMovieAdapter(this, this);
    }

    private void loadFavoriteMovies(){
        new LoadFavoriteAsync().execute();
    }

    private class LoadFavoriteAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor movie) {
            super.onPostExecute(movie);
            adapter.setFavoriteMovies(movie);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onItemClickListener(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        Uri uri = Uri.parse(CONTENT_URI + "/" + movie.getMovieId());
        intent.setData(uri);
        startActivity(intent);
    }
}
