package tseh20.wlunch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import tseh20.wlunch.data.DataContract.PostEntry;
import tseh20.wlunch.data.DataContract.CommentEntry;

/**
 * Created by ilia on 23.02.16.
 */
public class DbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "data.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_POSTS_TABLE = "CREATE TABLE " + PostEntry.TABLE_NAME + " (" +
                PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PostEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                PostEntry.COLUMN_BODY + " TEXT NOT NULL, " +
                PostEntry.COLUMN_USER_ID + " INTEGER NOT NULL);";

        final String SQL_CREATE_COMMENTS_TABLE = "CREATE TABLE " + CommentEntry.TABLE_NAME + " (" +
                CommentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CommentEntry.COLUMN_POST_ID + " INTEGER NOT NULL, " +
                CommentEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CommentEntry.COLUMN_BODY + " TEXT NOT NULL, " +
                CommentEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + CommentEntry.COLUMN_POST_ID + ") REFERENCES " +
                PostEntry.TABLE_NAME + " (" + PostEntry._ID + "));";

        sqLiteDatabase.execSQL(SQL_CREATE_POSTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_COMMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PostEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CommentEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
