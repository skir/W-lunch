package tseh20.wlunch.authorization;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

/**
 * Created by ilia on 26.02.16.
 */
public class TwitterController extends BaseController {

    TwitterAuthClient mClient;
    OnResult mCallback;

    public TwitterController(Context context) {
        super(context);
    }

    @Override
    public void login(Activity activity, OnResult callback) {
        mCallback = callback;
        if (mClient == null) {
            mClient = new TwitterAuthClient();
        }
        mClient.authorize(activity, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                Log.d("tw", "Logged with twitter");
                TwitterSession session = twitterSessionResult.data;
                save("twitter", String.valueOf(session.getUserId()));
                mCallback.onSuccess(session.getUserName());
            }

            @Override
            public void failure(com.twitter.sdk.android.core.TwitterException e) {
                Log.e("tw", "Failed login with twitter");
                e.printStackTrace();
            }
        });
    }

    public TwitterAuthClient getClient() {
        return mClient;
    }
}
