package sandhya.prabhu.in.ndmoviesapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import sandhya.prabhu.in.ndmoviesapp.R;



public class FavContentProvider extends ContentProvider {

    private FavouriteDBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new FavouriteDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        return database.query(FavouriteContract.FavouriteEntry.TABLE_NAME,
                strings,
                null,
                null,
                null, null, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Uri returnUri;
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long id = database.insert(FavouriteContract.FavouriteEntry.TABLE_NAME, null, contentValues);
        if (id > 0) {
            returnUri = ContentUris.withAppendedId(FavouriteContract.FavouriteEntry.CONTENT_URI, id);
        } else {
            throw new android.database.SQLException(getContext().getString(R.string.insert_failed) + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        return database.delete(FavouriteContract.FavouriteEntry.TABLE_NAME, s, null);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
