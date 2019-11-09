package sandhya.prabhu.in.ndmoviesapp.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.ndmoviesapp.R;
import sandhya.prabhu.in.ndmoviesapp.Utilities.JsonParserUtil;
import sandhya.prabhu.in.ndmoviesapp.Utilities.NetworkUtils;
import sandhya.prabhu.in.ndmoviesapp.adapter.ReviewAdapter;
import sandhya.prabhu.in.ndmoviesapp.adapter.TrailerAdapter;
import sandhya.prabhu.in.ndmoviesapp.asyncloaders.ReviewsAsyncLoader;
import sandhya.prabhu.in.ndmoviesapp.asyncloaders.TrailersAsyncLoader;
import sandhya.prabhu.in.ndmoviesapp.connection.ConnectionCheck;
import sandhya.prabhu.in.ndmoviesapp.data.FavouriteContract;
import sandhya.prabhu.in.ndmoviesapp.data.FavouriteDBHelper;
import sandhya.prabhu.in.ndmoviesapp.model.Review;
import sandhya.prabhu.in.ndmoviesapp.model.Trailer;
import sandhya.prabhu.in.ndmoviesapp.recycler.StatefulRecyclerView;


public class MovieDetailsActivity extends AppCompatActivity {

    private String original_title;
    private String title;
    private String poster;
    private String plotSynopsis;
    private String releaseDate;
    private Double userRating;
    private String backDrop;
    private int movie_id;
    private static final String TAG = MovieDetailsActivity.class.getName();
    public static final String TITLE = "title";
    public static final String ORIGINAL_TITLE = "originalTitle";
    public static final String MOVIE_POSTER = "moviePoster";
    public static final String PLOT_SYNOPSIS = "plotSynopsis";
    public static final String RATING = "rating";
    public static final String RELEASE_DATE = "releaseDate";
    public static final String MOVIE_ID = "movie_id";
    public static final String BACKDROP = "backdrop";

    private FavouriteDBHelper favouriteDBHelper;
    private List<Trailer> trailerList;
    private List<Review> reviewList;

    @BindView(R.id.heart_button)
    LikeButton likeButton;

    @BindView(R.id.loadingBar_review)
    public
    ContentLoadingProgressBar contentLoadingProgressBar1;

    @BindView(R.id.recyclerView)
    StatefulRecyclerView trailer_recyclerView;

    @BindView(R.id.review_recyclerView)
    StatefulRecyclerView review_recyclerView;

    @BindView(R.id.backdrop)
    ImageView backdrop_iv;

    @BindView(R.id.detailsPoster)
    ImageView poster_iv;

    @BindView(R.id.title)
    TextView title_tv;

    @BindView(R.id.user_rating)
    TextView rating_tv;

    @BindView(R.id.overview)
    TextView overview_tv;

    @BindView(R.id.release_date)
    TextView releaseDate_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        trailerList = new ArrayList<>();
        reviewList = new ArrayList<>();
        favouriteDBHelper = new FavouriteDBHelper(this);
        //This method fetches the data sent by MovieAdapter using Intent and populates the UI
        fetchData();

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (likeButton.isLiked()) {
                    saveFavourite();
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if (!likeButton.isLiked()) {
                    int i = getContentResolver().delete(FavouriteContract.FavouriteEntry.CONTENT_URI, FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID + "=" + movie_id, null);
                    if (i > 0) {
                        Toast.makeText(MovieDetailsActivity.this, getString(R.string.record_deleted), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void checkFav() {

        Cursor cursor = favouriteDBHelper.checkFavourites(movie_id);
        if (cursor.getCount() > 0) {
            likeButton.setLiked(true);
        } else {
            likeButton.setLiked(false);
        }
    }

    private void saveFavourite() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID, movie_id);
        contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_TITLE, title);
        contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_POSTER, poster);
        contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_BACKDROP, backDrop);
        contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_ORIGINAL_TITLE, original_title);
        contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_OVERVIEW, plotSynopsis);
        contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_RATING, userRating);
        contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_RELEASE_DATE, releaseDate);

        Uri uri = getContentResolver().insert(FavouriteContract.FavouriteEntry.CONTENT_URI, contentValues);
        if (uri != null) {
            Toast.makeText(this, R.string.saved_fav, Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchData() {
        Intent intent = getIntent();
        if (intent == null) {
            Log.d(TAG, getString(R.string.error_fetching_data_from_adapter));
        }
        original_title = intent.getExtras().getString(ORIGINAL_TITLE, "");
        title = intent.getExtras().getString(TITLE, "");
        poster = intent.getExtras().getString(MOVIE_POSTER, "");
        plotSynopsis = intent.getExtras().getString(PLOT_SYNOPSIS, "");
        releaseDate = intent.getExtras().getString(RELEASE_DATE, "");
        userRating = Double.parseDouble(intent.getExtras().getString(RATING, ""));
        backDrop = intent.getExtras().getString(BACKDROP, "");
        movie_id = intent.getExtras().getInt(MOVIE_ID, 0);
        populateUI();
    }

    private void populateUI() {
        Picasso.with(this)
                .load(MainActivity.POSTER_IMAGE + backDrop)
                .placeholder(R.mipmap.ic_launcher)
                .into(backdrop_iv);
        Picasso.with(this)
                .load(MainActivity.POSTER_IMAGE + poster)
                .placeholder(R.mipmap.ic_launcher)
                .into(poster_iv);

        if (!original_title.equals("")) {
            setTitle(original_title);
        } else {
            setTitle(R.string.app_name);
        }


        if (!title.equals("")) {
            title_tv.setText(title);
        } else {
            title_tv.setText(R.string.not_available_error);
        }
        if (!releaseDate.equals("")) {
            releaseDate_tv.setText(releaseDate);
        } else {
            releaseDate_tv.setText(R.string.not_available_error);
        }
        if (userRating != null) {
            rating_tv.setText(String.valueOf(userRating));
        } else {
            rating_tv.setText(R.string.not_available_error);
        }
        if (!plotSynopsis.equals("")) {
            overview_tv.setText(plotSynopsis);
        } else {
            overview_tv.setText(R.string.not_available_error);
        }

        boolean isInternetConnected = ConnectionCheck.checkConnection(this);
        if (isInternetConnected) {
            URL trailersUrl = NetworkUtils.buildTrailerAndReviewUrl(movie_id, NetworkUtils.VIDEO);
            TrailersAsyncLoader trailersAsyncLoader = new TrailersAsyncLoader(trailersUrl, this);
            trailersAsyncLoader.getTrailersList();

            URL reviewsUrl = NetworkUtils.buildTrailerAndReviewUrl(movie_id, NetworkUtils.REVIEW);
            ReviewsAsyncLoader reviewsAsyncLoader = new ReviewsAsyncLoader(reviewsUrl, this);
            reviewsAsyncLoader.getReviewsList();
        }
        checkFav();
    }

    public void loadTrailersinRecyclerView(String res) {
        if (res == null) {
            Toast.makeText(this, R.string.no_json_response, Toast.LENGTH_SHORT).show();
        } else {
            trailerList = JsonParserUtil.getTrailerParsingResp(res);
        }
        if (trailerList.isEmpty()) {
            Trailer trailer = new Trailer(getString(R.string.trailer_unavailable), null, null, null);
            trailerList.add(trailer);
            callAdapter();
        } else {
            callAdapter();
        }

    }

    private void callAdapter() {
        TrailerAdapter trailerAdapter = new TrailerAdapter(this, trailerList);
        trailer_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        trailer_recyclerView.setAdapter(trailerAdapter);
    }

    public void loadReviewsinRecyclerView(String res) {
        if (res == null) {
            Toast.makeText(this, R.string.no_json_response, Toast.LENGTH_SHORT).show();
        } else {
            reviewList = JsonParserUtil.getReviewsParsingResp(res);
        }
        if (reviewList.isEmpty()) {
            Review review = new Review(getString(R.string.review_unavailable), null);
            reviewList.add(review);
            callReviewAdapter();
        } else {
            callReviewAdapter();
        }

    }

    private void callReviewAdapter() {
        ReviewAdapter reviewAdapter = new ReviewAdapter(this, reviewList);
        review_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        review_recyclerView.setAdapter(reviewAdapter);
    }
}

