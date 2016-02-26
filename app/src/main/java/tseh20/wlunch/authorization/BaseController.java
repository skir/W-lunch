package tseh20.wlunch.authorization;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ilia on 26.02.16.
 */
public class BaseController {

    Context mContext;

    public BaseController(Context context) {
        mContext = context;
    }

    public void save(String loginMode, String id) {
        SharedPreferences preferences = mContext.getSharedPreferences("preferences", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("login_mode", loginMode);
        editor.putString("userId", id);
        editor.apply();
    }

    public void login(Activity activity, OnResult callback) {

    }

    public interface OnResult {
        void onSuccess(String userId);
    }
}
