package tseh20.wlunch.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ilia on 23.02.16.
 */
public class DataContract {

    public static final String CONTENT_AUTHORITY = "tseh20.wlunch";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_POSTS = "posts";
    public static final String PATH_COMMENTS = "comments";

    public static final class PostEntry implements BaseColumns {
        public static final String TABLE_NAME = PATH_POSTS;

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_USER_ID = "userId";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POSTS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POSTS;

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CommentEntry implements BaseColumns {
        public static final String TABLE_NAME = PATH_COMMENTS;

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_POST_ID = "postId";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMENTS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COMMENTS;

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildUriByPost(long postId) {
            return ContentUris.withAppendedId(CONTENT_URI.buildUpon().appendPath("bypost").build(), postId);
        }

        public static int getPostId(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(2));
        }
    }
}
