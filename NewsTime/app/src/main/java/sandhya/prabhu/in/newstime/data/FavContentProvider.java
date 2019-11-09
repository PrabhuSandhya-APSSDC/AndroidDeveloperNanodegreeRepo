package sandhya.prabhu.in.newstime.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import sandhya.prabhu.in.newstime.R;

public class FavContentProvider extends ContentProvider {

    private FavDBHelper favDBHelper;

    public FavContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        SQLiteDatabase database = favDBHelper.getWritableDatabase();
        return database.delete(FavContract.FavEntry.TABLE_NAME, selection, null);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException(getContext().getString(R.string.unsupported_op));
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri returnUri;
        SQLiteDatabase database = favDBHelper.getWritableDatabase();
        long id = database.insert(FavContract.FavEntry.TABLE_NAME, null, values);
        if (id > 0) {
            returnUri = ContentUris.withAppendedId(FavContract.FavEntry.CONTENT_URI, id);
        } else {
            throw new android.database.SQLException(getContext().getString(R.string.insert_failed) + uri);
        }
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        favDBHelper = new FavDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = favDBHelper.getReadableDatabase();
        return database.query(FavContract.FavEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException(getContext().getString(R.string.unsupported_op));
    }
}
