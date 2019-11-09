package sandhya.prabhu.in.ndmoviesapp.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class FavouriteContract {


    public static final class FavouriteEntry implements BaseColumns {

        public static final String AUTHORITY = "sandhya.prabhu.in.ndmoviesapp";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().build();

        public static final String TABLE_NAME = "favourite";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "movie_title";
        public static final String COLUMN_POSTER = "movie_poster";
        public static final String COLUMN_RATING = "movie_rating";
        public static final String COLUMN_BACKDROP = "movie_backdrop";
        public static final String COLUMN_ORIGINAL_TITLE = "movie_original_title";
        public static final String COLUMN_OVERVIEW = "movie_overview";
        public static final String COLUMN_RELEASE_DATE = "movie_date";

    }
}
