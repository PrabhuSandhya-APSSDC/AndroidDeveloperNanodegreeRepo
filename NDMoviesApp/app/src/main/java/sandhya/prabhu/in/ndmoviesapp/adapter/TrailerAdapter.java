package sandhya.prabhu.in.ndmoviesapp.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.ndmoviesapp.R;
import sandhya.prabhu.in.ndmoviesapp.activities.MovieDetailsActivity;
import sandhya.prabhu.in.ndmoviesapp.model.Trailer;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private MovieDetailsActivity movieDetailsActivity;
    private List<Trailer> trailerList;

    public TrailerAdapter(MovieDetailsActivity movieDetailsActivity, List<Trailer> trailerList) {
        this.movieDetailsActivity = movieDetailsActivity;
        this.trailerList = trailerList;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(movieDetailsActivity);
        View v = layoutInflater.inflate(R.layout.trailer_row, parent, false);
        return new TrailerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.trailerTitle.setText(trailerList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.trailer_title)
        TextView trailerTitle;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Trailer item = trailerList.get(pos);
                    String key = item.getKey();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                    movieDetailsActivity.startActivity(intent);
                }
            });
        }
    }
}
