package azuka.com.cataloguemovie.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import azuka.com.cataloguemovie.R;
import azuka.com.cataloguemovie.constants.Strings;
import azuka.com.cataloguemovie.listeners.RecyclerViewClickListener;
import azuka.com.cataloguemovie.models.Movie;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ivana Situmorang on 1/24/2019.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private RecyclerViewClickListener listener;
    private Context context;
    private Cursor cursor;

    public FavoriteAdapter(Context context, RecyclerViewClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setMovies(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new FavoriteViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Movie movie = getItem(position);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());
        Glide.with(context)
                .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.no_picture).error(R.drawable.no_picture))
                .load(Strings.POSTER_THUMB + movie.getPosterPath())
                .into(holder.ivThumb);
    }

    @Override
    public int getItemCount() {
        if (cursor == null) return 0;
        return cursor.getCount();
    }

    private Movie getItem(int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Position Invalid");
        }
        return new Movie(cursor);
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.tv_overview) TextView tvOverview;
        @BindView(R.id.iv_thumb) ImageView ivThumb;

        FavoriteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener == null) return;
            listener.onItemClickListener(getItem(getAdapterPosition()));
        }
    }
}
