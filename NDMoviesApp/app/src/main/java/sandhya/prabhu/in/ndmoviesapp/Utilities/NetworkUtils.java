package sandhya.prabhu.in.ndmoviesapp.Utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtils {
    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static final String MOVIE = "movie";
    public static final String VIDEO = "videos";
    public static final String REVIEW = "reviews";
    private static final String API_KEY = "80b691c51d9f2187f527e6d96cf09204";
    private static final String QUESRY_PARAM = "api_key";

    public static URL buildUrl(String typeOfSorting) {
        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(MOVIE)
                .appendPath(typeOfSorting)
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

    public static URL buildTrailerAndReviewUrl(int id, String type) {
        String movie_id = String.valueOf(id);
        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(MOVIE)
                .appendPath(movie_id)
                .appendPath(type)
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

    public static String getResponse(URL url) throws IOException {
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
