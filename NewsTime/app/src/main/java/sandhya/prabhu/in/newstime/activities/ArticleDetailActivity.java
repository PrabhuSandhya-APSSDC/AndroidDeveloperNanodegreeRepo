package sandhya.prabhu.in.newstime.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.like.LikeButton;
import com.like.OnLikeListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.newstime.FavouriteWidget;
import sandhya.prabhu.in.newstime.R;
import sandhya.prabhu.in.newstime.data.FavContract;
import sandhya.prabhu.in.newstime.data.FavDBHelper;

public class ArticleDetailActivity extends AppCompatActivity {

    public static final String ARTICLE_TITLE = "title";
    public static final String ARTICLE_AUTHOR = "author";
    public static final String ARTICLE_DESC = "desc";
    public static final String ARTICLE_RELATED_LINKS = "links";
    public static final String ARTICLE_IMAGE = "image";

    private String title;
    private String author;
    private String desc;
    private String links;
    private String image;

    @BindView(R.id.article_detail_image)
    ImageView image_tv;

    @BindView(R.id.article_detail_title)
    TextView title_tv;

    @BindView(R.id.article_detail_author)
    TextView author_tv;

    @BindView(R.id.article_detail_link)
    TextView link_tv;

    @BindView(R.id.article_detail_desc)
    TextView desc_tv;

    @BindView(R.id.heart_button)
    LikeButton likeButton;

    @BindView(R.id.adView)
    AdView adView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(getString(R.string.test_device_id))
                .build();
        adView.loadAd(adRequest);
        sharedPreferences = getSharedPreferences(getString(R.string.pref_file_name), MODE_PRIVATE);
        fetchAndSetData();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (likeButton.isLiked()) {
                    saveFavoutite();
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if (!likeButton.isLiked()) {

                    int i = getContentResolver().delete(FavContract.FavEntry.CONTENT_URI,
                            FavContract.FavEntry.COLUMN_ARTICLE_TITLE + "=" + "'" + title + "'"
                            , null);
                    if (i > 0) {
                        Toast.makeText(ArticleDetailActivity.this, R.string.deleted, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void saveFavoutite() {
        ContentValues contentValues = new ContentValues();
        title = title.replaceAll("'", "");
        contentValues.put(FavContract.FavEntry.COLUMN_ARTICLE_TITLE, title);
        contentValues.put(FavContract.FavEntry.COLUMN_ARTICLE_AUTHOR, author);
        contentValues.put(FavContract.FavEntry.COLUMN_ARTICLE_DESC, desc);
        contentValues.put(FavContract.FavEntry.COLUMN_ARTICLE_RELATED_LINKS, links);
        contentValues.put(FavContract.FavEntry.COLUMN_ARTICLE_IMAGE, image);

        Uri uri = getContentResolver().insert(FavContract.FavEntry.CONTENT_URI, contentValues);
        if (uri != null) {
            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(ARTICLE_TITLE, getString(R.string.last_viewed) + "\n\n" + title);
            editor.apply();
            callWidget();
        }
    }

    private void callWidget() {
        Intent inte = new Intent(this, FavouriteWidget.class);
        inte.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), FavouriteWidget.class));
        inte.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(inte);
    }

    private void fetchAndSetData() {
        Intent i = getIntent();
        if (i.hasExtra(ARTICLE_TITLE)) {
            title = i.getExtras().getString(ARTICLE_TITLE, "");
            author = i.getExtras().getString(ARTICLE_AUTHOR, "");
            desc = i.getExtras().getString(ARTICLE_DESC, "");
            links = i.getExtras().getString(ARTICLE_RELATED_LINKS, "");
            image = i.getExtras().getString(ARTICLE_IMAGE, "");
        }

        if (!title.equals("")) {
            title = title.replaceAll("'", "");
            title_tv.setText(title);
        } else {
            title_tv.setText(R.string.not_available);
        }
        if (!author.equals("")) {
            author_tv.setText(author);
        } else {
            author_tv.setText(R.string.not_available);
        }
        if (!desc.equals("")) {
            desc_tv.setText(desc);
        } else {
            desc_tv.setText(R.string.not_available);
        }
        if (!links.equals("")) {
            link_tv.setText(links);
        } else {
            link_tv.setText(R.string.not_available);
        }
        if (!image.equals("")) {
            Glide.with(this).load(Uri.parse(image)).placeholder(R.drawable.loading).into(image_tv);
        } else {
            image_tv.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
        }

        checkFav();
    }

    private void checkFav() {
        FavDBHelper favDBHelper = new FavDBHelper(this);
        Cursor cursor = favDBHelper.checkFavourites(title);
        if (cursor.getCount() > 0) {
            likeButton.setLiked(true);
        } else {
            likeButton.setLiked(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
