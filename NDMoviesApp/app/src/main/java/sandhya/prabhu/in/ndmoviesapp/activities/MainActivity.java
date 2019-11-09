package sandhya.prabhu.in.ndmoviesapp.activities;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.ndmoviesapp.R;
import sandhya.prabhu.in.ndmoviesapp.Utilities.JsonParserUtil;
import sandhya.prabhu.in.ndmoviesapp.Utilities.NetworkUtils;
import sandhya.prabhu.in.ndmoviesapp.adapter.MovieAdapter;
import sandhya.prabhu.in.ndmoviesapp.connection.ConnectionCheck;
import sandhya.prabhu.in.ndmoviesapp.data.FavouriteContract;
import sandhya.prabhu.in.ndmoviesapp.data.FavouriteDBHelper;
import sandhya.prabhu.in.ndmoviesapp.model.Movie;
import sandhya.prabhu.in.ndmoviesapp.recycler.StatefulRecyclerView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    @BindView(R.id.recyclerView)
    StatefulRecyclerView mRecyclerView;

    @BindView(R.id.loadingBar)
    ContentLoadingProgressBar contentLoadingProgressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.spinner)
    Spinner spinner;

    FavouriteDBHelper favouriteDBHelper;

    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String FAVOURITES = "fav";
    private List<Movie> movieList;
    private List<Movie> favList;
    Bundle m_bundle;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final int MOVIES_LOADER_ID = 777;
    private static final int FAV_LOADER_ID = 222;
    public static final String SEARCH_KEY = "query";
    public static final String POSTER_IMAGE = "https://image.tmdb.org/t/p/w500";
    MovieAdapter movieAdapter;
    public static String[] COLUMNS = {
            FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID,
            FavouriteContract.FavouriteEntry.COLUMN_TITLE,
            FavouriteContract.FavouriteEntry.COLUMN_POSTER,
            FavouriteContract.FavouriteEntry.COLUMN_BACKDROP,
            FavouriteContract.FavouriteEntry.COLUMN_ORIGINAL_TITLE,
            FavouriteContract.FavouriteEntry.COLUMN_OVERVIEW,
            FavouriteContract.FavouriteEntry.COLUMN_RATING,
            FavouriteContract.FavouriteEntry.COLUMN_RELEASE_DATE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isInternetConnected = ConnectionCheck.checkConnection(this);
        ButterKnife.bind(this);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        movieList = new ArrayList<>();
        favList = new ArrayList<>();
        favouriteDBHelper = new FavouriteDBHelper(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberofColumns()));
        m_bundle = new Bundle();
        m_bundle.putString(SEARCH_KEY, POPULAR);
        if (isInternetConnected) {
            Toast.makeText(this, R.string.con_suc, Toast.LENGTH_SHORT).show();
            getLoaderManager().initLoader(MOVIES_LOADER_ID, m_bundle, this);
        } else {
            getLoaderManager().initLoader(FAV_LOADER_ID, null, this);
        }

        sortBySpinner();
    }

    private int numberofColumns() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int widthDivider = 500;
        int width = displaymetrics.widthPixels;
        int coloumns = width / widthDivider;
        if (coloumns < 2) return 2;
        return coloumns;
    }

    private void sortBySpinner() {
        sharedPreferences = getSharedPreferences(getString(R.string.pref_file_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                (this,
                        R.layout.spinner_item,
                        getResources().getStringArray(R.array.spinner_list_items));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) spinner.getSelectedItem();
                if (selectedItem.equals(getString(R.string.popular_spinner_item))) {
                    editor.putString(SEARCH_KEY, POPULAR);
                    editor.apply();
                    callPopularLoader();
                } else if (selectedItem.equals(getString(R.string.top_rated_spinner_item))) {
                    editor.putString(SEARCH_KEY, TOP_RATED);
                    editor.apply();
                    callTopRatedLoader();
                } else if (selectedItem.equals(getString(R.string.fav_item))) {
                    callFavLoader();
                } else {
                    Toast.makeText(MainActivity.this, R.string.no_spinner_item_selected, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void callFavLoader() {
        if (!favList.isEmpty()) {
            favList.clear();
        }
        m_bundle.putString(SEARCH_KEY, FAVOURITES);
        editor.putString(SEARCH_KEY, FAVOURITES);
        editor.apply();
        getLoaderManager().restartLoader(FAV_LOADER_ID, m_bundle, MainActivity.this);
    }

    @Override
    public Loader<String> onCreateLoader(int i, final Bundle bundle) {
        switch (i) {
            case MOVIES_LOADER_ID:
                return new AsyncTaskLoader<String>(this) {

                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        if (bundle == null) {
                            return;
                        }
                        contentLoadingProgressBar.setVisibility(View.VISIBLE);
                        forceLoad();
                    }

                    @Override
                    public String loadInBackground() {
                        String typeOfMovie = bundle.getString(SEARCH_KEY, null);
                        try {
                            URL searchURL = NetworkUtils.buildUrl(typeOfMovie);
                            return NetworkUtils.getResponse(searchURL);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };

            case FAV_LOADER_ID:
                return new AsyncTaskLoader<String>(this) {
                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        contentLoadingProgressBar.setVisibility(View.VISIBLE);
                        forceLoad();
                    }

                    @Override
                    public String loadInBackground() {
                        Cursor cursor = getContentResolver().query(FavouriteContract.FavouriteEntry.CONTENT_URI, COLUMNS, null, null, null);
                        favList = getFavourites(cursor);
                        return null;
                    }
                };
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        contentLoadingProgressBar.setVisibility(View.INVISIBLE);
        switch (loader.getId()) {
            case MOVIES_LOADER_ID:
                if (s != null) {
                    movieList = JsonParserUtil.getParsingResponse(MainActivity.this, s);
                    movieAdapter = new MovieAdapter(MainActivity.this, movieList);
                    mRecyclerView.setAdapter(movieAdapter);
                }

                break;
            case FAV_LOADER_ID:
                if (!favList.isEmpty()) {
                    movieAdapter = new MovieAdapter(MainActivity.this, favList);
                    mRecyclerView.setAdapter(movieAdapter);
                } else {
                    Toast.makeText(this, R.string.no_favs, Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    private List<Movie> getFavourites(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID))),
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_RATING))),
                        cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_POSTER)),
                        cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_ORIGINAL_TITLE)),
                        cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_BACKDROP)),
                        cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_RELEASE_DATE)));
                favList.add(movie);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return favList;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sharedPreferences = getSharedPreferences(getString(R.string.pref_file_name), MODE_PRIVATE);
        if (sharedPreferences.getString(SEARCH_KEY, null).equals(FAVOURITES)) {
            callFavLoader();
        } else if (sharedPreferences.getString(SEARCH_KEY, null).equals(POPULAR)) {
            callPopularLoader();
        } else {
            callTopRatedLoader();
        }
    }

    private void callTopRatedLoader() {
        m_bundle.putString(SEARCH_KEY, TOP_RATED);
        getLoaderManager().restartLoader(MOVIES_LOADER_ID, m_bundle, MainActivity.this);
    }

    private void callPopularLoader() {
        m_bundle.putString(SEARCH_KEY, POPULAR);
        getLoaderManager().restartLoader(MOVIES_LOADER_ID, m_bundle, MainActivity.this);
    }


}
