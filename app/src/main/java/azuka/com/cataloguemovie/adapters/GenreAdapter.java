package azuka.com.cataloguemovie.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import azuka.com.cataloguemovie.R;
import azuka.com.cataloguemovie.models.Genre;

/**
 * Created by Ivana Situmorang on 1/24/2019.
 */
public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    private List<Genre> genres;
    private Genre genre;
    private Context context;

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        genre = genres.get(position);
        holder.tvGenre.setText(genre.getGenreName());
    }

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = ((genres.size() > 0) ? genres.size() : 0);
        } catch (Exception e) {
            Log.w("Eror: ", e.getMessage());
        }
        return count;
    }

    public GenreAdapter(Context context) {
        this.context = context;
    }

    public void setGenre(List<Genre> genres) {
        this.genres = genres;
    }

    class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView tvGenre;

        public GenreViewHolder(View itemView) {
            super(itemView);
            tvGenre = itemView.findViewById(R.id.tv_genre);
        }

    }
}
