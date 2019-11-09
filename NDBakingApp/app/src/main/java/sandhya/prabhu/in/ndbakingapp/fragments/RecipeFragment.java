package sandhya.prabhu.in.ndbakingapp.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.ndbakingapp.R;
import sandhya.prabhu.in.ndbakingapp.adapter.RecipeAdapter;
import sandhya.prabhu.in.ndbakingapp.connection.ConnectionCheck;
import sandhya.prabhu.in.ndbakingapp.model.Recipe;
import sandhya.prabhu.in.ndbakingapp.utilities.NetworkUtils;

public class RecipeFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar contentLoadingProgressBar;
    private static final String SEARCH_KEY = "query";
    private static final int BAKING_LOADER_ID = 777;
    private List<Recipe> recipes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recipe_frag, container, false);
        ButterKnife.bind(this, v);
        contentLoadingProgressBar.hide();
        boolean isInternetConnected = ConnectionCheck.checkConnection(getActivity());
        if (isInternetConnected) {
            Bundle bundle = new Bundle();
            bundle.putString(SEARCH_KEY, String.valueOf(NetworkUtils.buildUrl()));
            Loader<String> searchLoader = getLoaderManager().getLoader(BAKING_LOADER_ID);
            if (searchLoader == null) {
                getLoaderManager().initLoader(BAKING_LOADER_ID, bundle, this);
            } else {
                getLoaderManager().restartLoader(BAKING_LOADER_ID, bundle, this);
            }
        }
        return v;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {

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

            @Override
            public String loadInBackground() {
                String url = args.getString(SEARCH_KEY);
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
    public void onLoadFinished(Loader<String> loader, String data) {
        contentLoadingProgressBar.setVisibility(View.INVISIBLE);
        if (data != null && !data.equals("")) {
            loadRecyclerView(data);
        } else {
            Toast.makeText(getActivity(), getString(R.string.json_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }

    private void loadRecyclerView(String data) {
        recipes = parseJsonFromGson(data);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecipeAdapter recipeAdapter = new RecipeAdapter(getActivity(), recipes);
        recyclerView.setAdapter(recipeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private List<Recipe> parseJsonFromGson(String data) {
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<ArrayList<Recipe>>() {
        }.getType();
        recipes = gson.fromJson(data, listType);
        return recipes;
    }

}
