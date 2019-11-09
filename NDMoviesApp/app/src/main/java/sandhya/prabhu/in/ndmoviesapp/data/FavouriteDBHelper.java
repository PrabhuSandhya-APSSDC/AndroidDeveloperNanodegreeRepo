package sandhya.prabhu.in.ndmoviesapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FavouriteDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "fav.db";
    private static final int DATABASE_VERSION = 1;

    public FavouriteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_TABLE = "CREATE TABLE " + FavouriteContract.FavouriteEntry.TABLE_NAME + " (" +
                FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, " +
                FavouriteContract.FavouriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavouriteContract.FavouriteEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                FavouriteContract.FavouriteEntry.COLUMN_BACKDROP + " TEXT NOT NULL, " +
                FavouriteContract.FavouriteEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                FavouriteContract.FavouriteEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavouriteContract.FavouriteEntry.COLUMN_RATING + " DOUBLE NOT NULL, " +
                FavouriteContract.FavouriteEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL " +
                ");";
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouriteContract.FavouriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public Cursor checkFavourites(int movie_id) {
        String[] COLUMNS = {
                FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID,
                FavouriteContract.FavouriteEntry.COLUMN_TITLE,
                FavouriteContract.FavouriteEntry.COLUMN_POSTER
        };

       return getReadableDatabase().query(FavouriteContract.FavouriteEntry.TABLE_NAME,
                COLUMNS,
                FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID + "=?",
                new String[]{String.valueOf(movie_id)}, null, null, null);
    }
}
