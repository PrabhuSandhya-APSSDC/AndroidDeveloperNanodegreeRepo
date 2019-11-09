package sandhya.prabhu.in.ndmoviesapp.adapter;

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
import sandhya.prabhu.in.ndmoviesapp.model.Review;



public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private MovieDetailsActivity context;
    private List<Review> reviews;

    public ReviewAdapter(MovieDetailsActivity movieDetailsActivity, List<Review> reviewList) {
        context = movieDetailsActivity;
        reviews = reviewList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.review_row, parent, false);
        return new ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {


        holder.author.setText(reviews.get(position).getAuthor());
        holder.content.setText(reviews.get(position).getContent());


    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.author)
        TextView author;

        @BindView(R.id.content)
        TextView content;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
