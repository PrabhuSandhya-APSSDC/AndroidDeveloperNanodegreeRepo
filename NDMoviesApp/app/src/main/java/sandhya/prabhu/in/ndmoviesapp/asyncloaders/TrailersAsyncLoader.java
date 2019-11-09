package sandhya.prabhu.in.ndmoviesapp.asyncloaders;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

import sandhya.prabhu.in.ndmoviesapp.R;
import sandhya.prabhu.in.ndmoviesapp.Utilities.NetworkUtils;
import sandhya.prabhu.in.ndmoviesapp.activities.MovieDetailsActivity;

import static sandhya.prabhu.in.ndmoviesapp.activities.MainActivity.SEARCH_KEY;


public class TrailersAsyncLoader implements LoaderManager.LoaderCallbacks<String> {
    private static final int TRAILERS_LOADER_ID = 999;
    private MovieDetailsActivity movieDetailsActivity;
    private URL trailerUrl;

    public TrailersAsyncLoader(URL trailerUrl, MovieDetailsActivity movieDetailsActivity) {
        this.movieDetailsActivity = movieDetailsActivity;
        this.trailerUrl = trailerUrl;
    }

    public void getTrailersList() {
        Loader<Object> loader = movieDetailsActivity.getLoaderManager().getLoader(TRAILERS_LOADER_ID);
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_KEY, trailerUrl.toString());
        if (loader == null) {
            movieDetailsActivity.getLoaderManager().initLoader(TRAILERS_LOADER_ID, bundle, this);
        } else {
            movieDetailsActivity.getLoaderManager().restartLoader(TRAILERS_LOADER_ID, bundle, this);
        }

    }

    @Override
    public Loader<String> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<String>(movieDetailsActivity) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (bundle == null) {
                    Log.d(movieDetailsActivity.getString(R.string.error_tag), movieDetailsActivity.getString(R.string.error_fetching_data));
                    return;
                }
                movieDetailsActivity.contentLoadingProgressBar1.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                String searchUrl = bundle.getString(SEARCH_KEY);
                Log.d(movieDetailsActivity.getString(R.string.search_url_tag), searchUrl);
                if (searchUrl == null || TextUtils.isEmpty(searchUrl)) {
                    Toast.makeText(movieDetailsActivity, R.string.error_fetching_url, Toast.LENGTH_SHORT).show();
                }
                try {
                    URL url = new URL(searchUrl);
                    return NetworkUtils.getResponse(url);
                }  catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String res) {
        movieDetailsActivity.contentLoadingProgressBar1.setVisibility(View.INVISIBLE);
        if (res != null && !res.equals("")) {
            movieDetailsActivity.loadTrailersinRecyclerView(res);
        } else {
            Toast.makeText(movieDetailsActivity, R.string.error_json_data, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
