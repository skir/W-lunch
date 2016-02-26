package tseh20.wlunch.activities;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import tseh20.wlunch.R;
import tseh20.wlunch.fragments.LoginForAuthorized;
import tseh20.wlunch.fragments.LoginPhoneFragment;
import tseh20.wlunch.fragments.LoginSelectorFragment;
import tseh20.wlunch.tools.TypefaceManager;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.app_name)
    TextView mAppName;

    LoginPhoneFragment mLoginPhoneFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAppName.setTypeface(TypefaceManager.getLightTypeface(this));

        SharedPreferences preferences = getSharedPreferences("preferences", 0);
        String loginMode = preferences.getString("login_mode", "");
        String userId = preferences.getString("userId", "");
        if (loginMode.equals("")) {
            getFragmentManager().beginTransaction().add(R.id.fragment, new LoginSelectorFragment(), "").commit();
        } else if (loginMode.equals("phone")) {
            LoginForAuthorized loginForAuthorized = new LoginForAuthorized();
            loginForAuthorized.setPhone(userId);
            getFragmentManager().beginTransaction().add(R.id.fragment, loginForAuthorized, "").commit();
        } else if (!userId.equals("")) {
                Toast.makeText(this, "Signed in with id " + userId, Toast.LENGTH_LONG).show();
                startMainActivity();
        }
    }

    public void loginWithPhone() {
        mLoginPhoneFragment = new LoginPhoneFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment, mLoginPhoneFragment, "").commit();
    }

    @Override
    public void onBackPressed() {
        if (mLoginPhoneFragment != null) {
            mLoginPhoneFragment.onBackPressed();
        }
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the fragment, which will then pass the result to the login
        // button.
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
