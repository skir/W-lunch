package tseh20.wlunch.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by ilia on 23.02.16.
 */
public class DataProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DbHelper mOpenHelper;

    static final int POST = 100;
    static final int COMMENT = 200;
    static final int COMMENT_BY_POST = 201;

    static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_POSTS, POST);
        uriMatcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_COMMENTS, COMMENT);
        uriMatcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_COMMENTS + "/bypost/#", COMMENT_BY_POST);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case POST:
                return DataContract.PostEntry.CONTENT_TYPE;
            case COMMENT:
                return DataContract.CommentEntry.CONTENT_TYPE;
            case COMMENT_BY_POST:
                return DataContract.CommentEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case POST:
                retCursor = mOpenHelper.getReadableDatabase().query(DataContract.PostEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                retCursor.setNotificationUri(getContext().getContentResolver(), DataContract.PostEntry.CONTENT_URI);
                break;
            case COMMENT_BY_POST:
                retCursor = mOpenHelper.getReadableDatabase().query(DataContract.CommentEntry.TABLE_NAME,
                        projection,
                        DataContract.CommentEntry.TABLE_NAME + "." + DataContract.CommentEntry.COLUMN_POST_ID + " = ? ",
                        new String[]{String.valueOf(DataContract.CommentEntry.getPostId(uri))},
                        null,
                        null,
                        sortOrder);
                retCursor.setNotificationUri(getContext().getContentResolver(), DataContract.CommentEntry.CONTENT_URI);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return retCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id;

        switch (match) {
            case POST:
                id = db.insert(DataContract.PostEntry.TABLE_NAME, null, values);
                if (id > 0)
                    returnUri = DataContract.PostEntry.buildUri(id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;

            case COMMENT:
                id = db.insert(DataContract.CommentEntry.TABLE_NAME, null, values);
                if (id > 0)
                    returnUri = DataContract.CommentEntry.buildUri(id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int id;
        if (selection == null) selection = "1";
        switch (match) {
            case POST:
                id = db.delete(DataContract.PostEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case COMMENT:
                id = db.delete(DataContract.CommentEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(id != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int id;

        switch (match) {
            case POST:
                id = db.update(DataContract.PostEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case COMMENT:
                id = db.update(DataContract.CommentEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(id != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case POST: {
                int returnCount = bulkInsert(DataContract.PostEntry.TABLE_NAME, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case COMMENT: {
                int returnCount = bulkInsert(DataContract.CommentEntry.TABLE_NAME, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private int bulkInsert(String tableName, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {

                long _id = db.insert(tableName, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return returnCount;
    }
}
