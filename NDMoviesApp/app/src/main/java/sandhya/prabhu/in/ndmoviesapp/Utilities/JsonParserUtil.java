package sandhya.prabhu.in.ndmoviesapp.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sandhya.prabhu.in.ndmoviesapp.R;
import sandhya.prabhu.in.ndmoviesapp.activities.MainActivity;
import sandhya.prabhu.in.ndmoviesapp.model.Movie;
import sandhya.prabhu.in.ndmoviesapp.model.Review;
import sandhya.prabhu.in.ndmoviesapp.model.Trailer;


public class JsonParserUtil {

    public static List<Movie> getParsingResponse(MainActivity mainActivity, String res) {
        List<Movie> movies_list = new ArrayList<>();
        try {
            JSONObject parentObject = new JSONObject(res);
            JSONArray resultsArray = parentObject.optJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject movieListJson = resultsArray.getJSONObject(i);
                int id = movieListJson.optInt("id", 299536);
                String title = movieListJson.optString("title", mainActivity.getString(R.string.def_title));
                String backDropPath = movieListJson.optString("backdrop_path", mainActivity.getString(R.string.def_back_drop));
                String originalTitle = movieListJson.optString("original_title", mainActivity.getString(R.string.def_original_title));
                String posterPath = movieListJson.optString("poster_path", mainActivity.getString(R.string.def_poster_path));
                String ploySysnopsis = movieListJson.optString("overview", mainActivity.getString(R.string.def_overview));
                Double rating = movieListJson.optDouble("vote_average", 8.5);
                String releaseDate = movieListJson.optString("release_date", mainActivity.getString(R.string.def_release_date));
                Movie movie = new Movie(id, rating, title, posterPath, originalTitle, backDropPath, ploySysnopsis, releaseDate);
                movies_list.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies_list;
    }

    public static List<Trailer> getTrailerParsingResp(String res) {
        List<Trailer> trailerList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject trilerObj = jsonArray.getJSONObject(i);
                String key = trilerObj.optString("key", null);
                String name = trilerObj.optString("name", null);
                String site = trilerObj.optString("site", null);
                String type = trilerObj.optString("type", null);
                Trailer trailer = new Trailer(key, name, site, type);
                trailerList.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailerList;
    }

    public static List<Review> getReviewsParsingResp(String res) {
        List<Review> reviewList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject reviewObj = jsonArray.getJSONObject(i);
                String author = reviewObj.optString("author", null);
                String content = reviewObj.optString("content", null);
                Review review = new Review(author, content);
                reviewList.add(review);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewList;
    }
}
