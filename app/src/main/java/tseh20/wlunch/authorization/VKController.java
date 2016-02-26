package tseh20.wlunch.authorization;

import android.app.Activity;
import android.content.Context;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

/**
 * Created by ilia on 22.02.16.
 */
public class VKController extends BaseController {

    OnResult mCallback;

    public VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken != null) {
                save("vk", newToken.userId);
                mCallback.onSuccess(newToken.userId);
            }
        }
    };

    @Override
    public void login(Activity activity, OnResult callback) {
        VKSdk.login(activity);
        mCallback = callback;
    }

    public void logOut() {
        VKSdk.logout();
    }

    public VKController(Context context) {
        super(context);
        vkAccessTokenTracker.startTracking();
    }
}
