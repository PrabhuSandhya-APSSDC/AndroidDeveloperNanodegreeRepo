package sandhya.prabhu.in.newstime.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.newstime.R;
import sandhya.prabhu.in.newstime.activities.MainActivity;
import sandhya.prabhu.in.newstime.adapter.ArticleAdapter;
import sandhya.prabhu.in.newstime.data.FavContract;
import sandhya.prabhu.in.newstime.model.Article;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavFragment extends Fragment /*implements LoaderManager.LoaderCallbacks<String>*/ {

    @BindView(R.id.articleRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar contentLoadingProgressBar;

    private List<Article> favList;
    public static final String SAVE_LIST_STATE = "save_state";
    GridLayoutManager gridLayoutManager;
    Parcelable listSate;

    String[] COLUMNS = {
            FavContract.FavEntry.COLUMN_ARTICLE_TITLE,
            FavContract.FavEntry.COLUMN_ARTICLE_AUTHOR,
            FavContract.FavEntry.COLUMN_ARTICLE_DESC,
            FavContract.FavEntry.COLUMN_ARTICLE_RELATED_LINKS,
            FavContract.FavEntry.COLUMN_ARTICLE_IMAGE
    };

    public FavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_articles, container, false);
        ButterKnife.bind(this, v);
        contentLoadingProgressBar.hide();
        getActivity().setTitle(getString(R.string.fav));
        favList = new ArrayList<>();
        if (favList != null) {
            favList.clear();
        }
        gridLayoutManager = new GridLayoutManager(getActivity(), numberofColumns());
        getData();
        return v;
    }

    private void getData() {
        Cursor cursor = getActivity().getContentResolver().query(FavContract.FavEntry.CONTENT_URI, COLUMNS, null, null, null);
        favList = getFavourites(cursor);
    }

    private List<Article> getFavourites(Cursor cursor) {

        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            do {
                Article article = new Article();
                article.setTitle(cursor.getString(cursor.getColumnIndex(FavContract.FavEntry.COLUMN_ARTICLE_TITLE)));
                article.setAuthor(cursor.getString(cursor.getColumnIndex(FavContract.FavEntry.COLUMN_ARTICLE_AUTHOR)));
                article.setDescription(cursor.getString(cursor.getColumnIndex(FavContract.FavEntry.COLUMN_ARTICLE_DESC)));
                article.setUrl(cursor.getString(cursor.getColumnIndex(FavContract.FavEntry.COLUMN_ARTICLE_RELATED_LINKS)));
                article.setUrlToImage(cursor.getString(cursor.getColumnIndex(FavContract.FavEntry.COLUMN_ARTICLE_IMAGE)));
                favList.add(article);
            } while (cursor.moveToNext());
            ArticleAdapter articleAdapter = new ArticleAdapter(getActivity(), favList);
            recyclerView.setAdapter(articleAdapter);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {

            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.no_favs_selected)
                    .setMessage(R.string.alert_msg)
                    .setCancelable(true)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();

                        }
                    })
                    .show();
        }
        return favList;
    }

    private int numberofColumns() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int widthDivider = 500;
        int width = displaymetrics.widthPixels;
        int coloumns = width / widthDivider;
        if (coloumns < 2) return 2;
        return coloumns;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(listSate!=null)
        {
            gridLayoutManager.onRestoreInstanceState(listSate);
        }
        if (favList != null) {
            favList.clear();
            getData();
        } else {
            getData();
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listSate = gridLayoutManager.onSaveInstanceState();
        outState.putParcelable(SAVE_LIST_STATE,listSate);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null)
        {
            listSate=savedInstanceState.getParcelable(SAVE_LIST_STATE);
        }
    }
}