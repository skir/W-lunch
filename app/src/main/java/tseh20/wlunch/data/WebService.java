package tseh20.wlunch.data;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tseh20.wlunch.data.model.Comment;
import tseh20.wlunch.data.model.Post;

/**
 * Created by ilia on 23.02.16.
 */
public class WebService {

    API mService;
    Context mContext;
    static WebService mWebService;

    public static WebService getWebservice(Context context) {
        if (mWebService == null) {
            mWebService = new WebService(context);
        }
        return mWebService;
    }

    private WebService(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(API.class);
        mContext = context;
    }

    public void getPosts() {
        Call<List<Post>> posts = mService.getPosts();
        posts.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                Log.e("post", String.valueOf(response.body().size()));

                Vector<ContentValues> valuesVector = new Vector<>(response.body().size());
                for (Post post: response.body()) {
                    ContentValues values = new ContentValues();
                    values.put(DataContract.PostEntry.COLUMN_TITLE, post.title);
                    values.put(DataContract.PostEntry.COLUMN_BODY, post.body);
                    values.put(DataContract.PostEntry.COLUMN_USER_ID, post.userId);
                    values.put(DataContract.PostEntry._ID, post.id);
                    valuesVector.add(values);
                }

                if(valuesVector.size() > 0){
                    ContentValues[] contentValues = new ContentValues[valuesVector.size()];
                    valuesVector.toArray(contentValues);

                    mContext.getContentResolver().bulkInsert(DataContract.PostEntry.CONTENT_URI, contentValues);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    public void getComments(int postId) {
        Call<List<Comment>> comments = mService.getComments(postId);
        comments.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                Log.e("comment", String.valueOf(response.body().size()));

                Vector<ContentValues> valuesVector = new Vector<>(response.body().size());
                for (Comment comment : response.body()) {
                    ContentValues values = new ContentValues();
                    values.put(DataContract.CommentEntry.COLUMN_NAME, comment.name);
                    values.put(DataContract.CommentEntry.COLUMN_BODY, comment.body);
                    values.put(DataContract.CommentEntry.COLUMN_EMAIL, comment.email);
                    values.put(DataContract.CommentEntry.COLUMN_POST_ID, comment.postId);
                    values.put(DataContract.CommentEntry._ID, comment.id);
                    valuesVector.add(values);
                }

                if (valuesVector.size() > 0) {
                    ContentValues[] contentValues = new ContentValues[valuesVector.size()];
                    valuesVector.toArray(contentValues);

                    mContext.getContentResolver().bulkInsert(DataContract.CommentEntry.CONTENT_URI, contentValues);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    public interface API{
        @GET("/posts")
        Call<List<Post>> getPosts();

        @GET("/comments")
        Call<List<Comment>> getComments(@Query("postId") int postId);
    }
}
