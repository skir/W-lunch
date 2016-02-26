package tseh20.wlunch;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.vk.sdk.VKSdk;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

/**
 * Created by ilia on 24.02.16.
 */
public class WlunchApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "VQTkj6t8vtfZGqJJObmLKaEqb";
    private static final String TWITTER_SECRET = "GqxCNLcRGiBSC3JjOnY8ulSTJRQpuDDaA3yr5RM9Jrse9yjLCN";



    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        VKSdk.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
