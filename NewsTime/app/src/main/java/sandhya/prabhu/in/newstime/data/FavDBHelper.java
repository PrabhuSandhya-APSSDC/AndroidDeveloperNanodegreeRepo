package sandhya.prabhu.in.newstime.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class FavDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "fav.db";
    private static final int DATABASE_VERSION = 1;

    public FavDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_TABLE = "CREATE TABLE " + FavContract.FavEntry.TABLE_NAME + " (" +
                FavContract.FavEntry._ID + " INTEGER PRIMARY KEY, " +
                FavContract.FavEntry.COLUMN_ARTICLE_TITLE + " TEXT NOT NULL, " +
                FavContract.FavEntry.COLUMN_ARTICLE_AUTHOR + " TEXT NOT NULL, " +
                FavContract.FavEntry.COLUMN_ARTICLE_DESC + " TEXT NOT NULL, " +
                FavContract.FavEntry.COLUMN_ARTICLE_RELATED_LINKS + " TEXT NOT NULL, " +
                FavContract.FavEntry.COLUMN_ARTICLE_IMAGE + " TEXT NOT NULL " +
                ");";

        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavContract.FavEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public Cursor checkFavourites(String title) {

        String[] COLUMNS = {
                FavContract.FavEntry.COLUMN_ARTICLE_TITLE,
                FavContract.FavEntry.COLUMN_ARTICLE_AUTHOR,
                FavContract.FavEntry.COLUMN_ARTICLE_DESC,
                FavContract.FavEntry.COLUMN_ARTICLE_RELATED_LINKS,
                FavContract.FavEntry.COLUMN_ARTICLE_IMAGE
        };

        return getReadableDatabase().query(FavContract.FavEntry.TABLE_NAME,
                COLUMNS,
                FavContract.FavEntry.COLUMN_ARTICLE_TITLE + "=?",
                new String[]{title}, null, null, null
        );
    }
}
