package tseh20.wlunch.authorization;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Collections;

/**
 * Created by ilia on 26.02.16.
 */
public class FacebookController extends BaseController {

    CallbackManager mCallbackManager;

    public FacebookController(Context context) {
        super(context);
    }

    @Override
    public void login(Activity activity, OnResult callback) {
        LoginManager loginManager = LoginManager.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                save("facebook", accessToken.getUserId());
                callback.onSuccess(accessToken.getUserId());
            }

            @Override
            public void onCancel() {
                Log.e("fb", "Canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("fb", "Error");
            }
        });

        loginManager.logInWithReadPermissions(activity, Collections.singletonList("public_profile"));
    }

    public CallbackManager getCallbackManager() {
        return mCallbackManager;
    }
}
