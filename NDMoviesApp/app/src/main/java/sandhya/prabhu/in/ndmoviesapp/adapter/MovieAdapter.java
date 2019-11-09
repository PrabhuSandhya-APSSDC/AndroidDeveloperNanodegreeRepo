package sandhya.prabhu.in.ndmoviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.ndmoviesapp.activities.MainActivity;
import sandhya.prabhu.in.ndmoviesapp.R;
import sandhya.prabhu.in.ndmoviesapp.activities.MovieDetailsActivity;
import sandhya.prabhu.in.ndmoviesapp.model.Movie;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final Context context;
    private final List<Movie> movieList;

    public MovieAdapter(MainActivity mainActivity, List<Movie> movieList) {
        context = mainActivity;
        this.movieList = movieList;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.movie_row, parent, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {

        Picasso.with(context)
                .load(MainActivity.POSTER_IMAGE + movieList.get(position).getPoster_path())
                .placeholder(R.drawable.loading)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail)
        ImageView imageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int postition = getAdapterPosition();
                    Movie item = movieList.get(postition);
                    Intent intent = new Intent(context, MovieDetailsActivity.class);
                    intent.putExtra(MovieDetailsActivity.TITLE, item.getTitle());
                    intent.putExtra(MovieDetailsActivity.ORIGINAL_TITLE, item.getOriginal_title());
                    intent.putExtra(MovieDetailsActivity.MOVIE_POSTER, item.getPoster_path());
                    intent.putExtra(MovieDetailsActivity.PLOT_SYNOPSIS, item.getOverview());
                    intent.putExtra(MovieDetailsActivity.RATING, String.valueOf(item.getVote_average()));
                    intent.putExtra(MovieDetailsActivity.RELEASE_DATE, item.getRelease_date());
                    intent.putExtra(MovieDetailsActivity.MOVIE_ID, item.getId());
                    intent.putExtra(MovieDetailsActivity.BACKDROP, item.getBackdrop_path());
                    context.startActivity(intent);
                }
            });
        }
    }
}
