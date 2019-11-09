package sandhya.prabhu.in.newstime.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class FavContract {
    public static final class FavEntry implements BaseColumns {
        public static final String AUTHORITY = "sandhya.prabhu.in.newstime";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().build();
        public static final String TABLE_NAME = "fav_table";
        public static final String COLUMN_ARTICLE_TITLE = "title";
        public static final String COLUMN_ARTICLE_AUTHOR = "author";
        public static final String COLUMN_ARTICLE_DESC = "desc";
        public static final String COLUMN_ARTICLE_RELATED_LINKS = "links";
        public static final String COLUMN_ARTICLE_IMAGE = "image";

    }
}
