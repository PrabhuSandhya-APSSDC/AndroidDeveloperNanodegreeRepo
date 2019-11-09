package sandhya.prabhu.in.newstime.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public final class NetworkUtils {
    private static final String NEWS_BASE_URL = "https://newsapi.org/v2/top-headlines?";
    private static final String ALL_NEWS_BASE_URL = "https://newsapi.org/v2/everything?";
    private static final String CODE = "country";
    private static final String CATEGORY = "category";
    private static final String SEARCH_QUERY_PARAM = "q";
    private static final String API_KEY = "68a29e2dab714a26ae547f5256bb83f0";
    private static final String QUESRY_PARAM = "apiKey";

    public static URL buildSearchUrl(String searchString) {
        Uri builtUri = Uri.parse(ALL_NEWS_BASE_URL)
                .buildUpon()
                .appendQueryParameter(SEARCH_QUERY_PARAM, searchString)
                .appendQueryParameter(QUESRY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrl(String country_code) {
        Uri builtUri = Uri.parse(NEWS_BASE_URL)
                .buildUpon()
                .appendQueryParameter(CODE, country_code)
                .appendQueryParameter(QUESRY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildCategoryUrl(String country_code, String category) {
        Uri builtUri = Uri.parse(NEWS_BASE_URL)
                .buildUpon()
                .appendQueryParameter(CODE, country_code)
                .appendQueryParameter(CATEGORY, category)
                .appendQueryParameter(QUESRY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}