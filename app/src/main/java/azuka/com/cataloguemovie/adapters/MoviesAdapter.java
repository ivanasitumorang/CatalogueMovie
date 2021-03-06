package azuka.com.cataloguemovie.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import azuka.com.cataloguemovie.R;
import azuka.com.cataloguemovie.constants.Strings;
import azuka.com.cataloguemovie.listeners.RecyclerViewClickListener;
import azuka.com.cataloguemovie.models.Movie;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ivana Situmorang on 1/24/2019.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private RecyclerViewClickListener listener;
    private ArrayList<Movie> movieList;
    private Context context;
    private Cursor cursor;

    public MoviesAdapter(Context context, RecyclerViewClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setMovies(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }

    public void setMovies(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MoviesViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());
        Glide.with(context)
                .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.no_picture).error(R.drawable.no_picture))
                .load(Strings.POSTER_THUMB + movie.getPosterPath())
                .into(holder.ivThumb);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = ((movieList.size() > 0) ? movieList.size() : 0);
        } catch (Exception e) {
            Log.w("Eror: ", e.getMessage());
        }
        return count;
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.tv_overview) TextView tvOverview;
        @BindView(R.id.iv_thumb) ImageView ivThumb;

        MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener == null) return;
            listener.onItemClickListener(movieList.get(getAdapterPosition()));
        }
    }
}
