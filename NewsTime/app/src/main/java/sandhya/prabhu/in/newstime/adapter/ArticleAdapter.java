package sandhya.prabhu.in.newstime.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.newstime.R;
import sandhya.prabhu.in.newstime.activities.ArticleDetailActivity;
import sandhya.prabhu.in.newstime.model.Article;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private FragmentActivity activity;
    private List<Article> articleList;

    public ArticleAdapter(FragmentActivity activity, List<Article> articleList) {
        this.activity = activity;
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.article_row, parent, false);
        return new ArticleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {

        Article item = articleList.get(position);
        if (item.getUrlToImage() == null) {
            holder.articleImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.no_image));
        } else {
            Glide.with(activity).load(item.getUrlToImage()).placeholder(R.drawable.loading)
                    .into(holder.articleImage);
        }

        holder.articleTitle.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.articleImage)
        ImageView articleImage;

        @BindView(R.id.articleTitle)
        TextView articleTitle;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Article article = articleList.get(pos);
                    Intent intent = new Intent(activity, ArticleDetailActivity.class);
                    intent.putExtra(ArticleDetailActivity.ARTICLE_TITLE, article.getTitle());
                    intent.putExtra(ArticleDetailActivity.ARTICLE_AUTHOR, article.getAuthor());
                    intent.putExtra(ArticleDetailActivity.ARTICLE_DESC, article.getDescription());
                    intent.putExtra(ArticleDetailActivity.ARTICLE_RELATED_LINKS, article.getUrl());
                    intent.putExtra(ArticleDetailActivity.ARTICLE_IMAGE, article.getUrlToImage());
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity
                            , articleImage, ViewCompat.getTransitionName(articleImage));
                    activity.startActivity(intent, optionsCompat.toBundle());
                }
            });
        }
    }
}
