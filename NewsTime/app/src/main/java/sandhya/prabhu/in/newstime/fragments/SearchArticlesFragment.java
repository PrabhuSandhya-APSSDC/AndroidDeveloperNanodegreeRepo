package sandhya.prabhu.in.newstime.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.newstime.R;
import sandhya.prabhu.in.newstime.activities.NewsCategoriesActivity;
import sandhya.prabhu.in.newstime.adapter.ArticleAdapter;
import sandhya.prabhu.in.newstime.connection.ConnectionCheck;
import sandhya.prabhu.in.newstime.model.Article;
import sandhya.prabhu.in.newstime.model.News;
import sandhya.prabhu.in.newstime.utilities.NetworkUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchArticlesFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    @BindView(R.id.articleRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar contentLoadingProgressBar;

    private static final int SEARCH_LOADER_ID = 999;

    private String searchString = "";

    private String url;

    private News news;
    GridLayoutManager gridLayoutManager;
    Parcelable listSate;

    private static final String SEARCH = "search";
    public static final String SAVE_LIST_STATE = "save_state";


    public SearchArticlesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_articles, container, false);
        ButterKnife.bind(this, v);
        contentLoadingProgressBar.hide();
        if (getArguments().containsKey(NewsCategoriesActivity.SEARCH_STRING)) {
            searchString = getArguments().getString(NewsCategoriesActivity.SEARCH_STRING, "");
        }
        boolean isInternetConnected = ConnectionCheck.checkConnection(getActivity());
        gridLayoutManager = new GridLayoutManager(getActivity(), numberofColumns());
        if (isInternetConnected) {
            if (!searchString.equals("")) {
                url = NetworkUtils.buildSearchUrl(searchString).toString();
            }
            Bundle bundle = new Bundle();
            bundle.putString(SEARCH, url);
            Loader<String> searchLoader = getLoaderManager().getLoader(SEARCH_LOADER_ID);
            if (searchLoader == null) {
                getLoaderManager().initLoader(SEARCH_LOADER_ID, bundle, this);
            } else {
                getLoaderManager().restartLoader(SEARCH_LOADER_ID, bundle, this);
            }
            getActivity().setTitle(getString(R.string.search_res_title));
        }
        return v;
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<String>(getActivity()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }
                contentLoadingProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Nullable
            @Override
            public String loadInBackground() {
                String url = args.getString(SEARCH);
                if (url == null || TextUtils.isEmpty(url)) {
                    return null;
                }
                try {
                    URL searchUrl = new URL(url);
                    return NetworkUtils.getResponseFromHttpUrl(searchUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        contentLoadingProgressBar.setVisibility(View.INVISIBLE);
        Log.d(getString(R.string.json_data), data.toString());
        if (data != null && !data.equals("")) {
            news = new News();
            news = parseJsonfromGson(data);
            List<Article> articleList = news.getArticles();
            ArticleAdapter articleAdapter = new ArticleAdapter(getActivity(), articleList);
            recyclerView.setAdapter(articleAdapter);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            Toast.makeText(getActivity(), R.string.json_error, Toast.LENGTH_SHORT).show();
        }
    }

    private News parseJsonfromGson(String data) {
        Gson gson = new GsonBuilder().create();
        news = gson.fromJson(data, News.class);
        return news;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listSate = gridLayoutManager.onSaveInstanceState();
        outState.putParcelable(SAVE_LIST_STATE, listSate);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            listSate = savedInstanceState.getParcelable(SAVE_LIST_STATE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listSate != null) {
            gridLayoutManager.onRestoreInstanceState(listSate);
        }
    }
}
