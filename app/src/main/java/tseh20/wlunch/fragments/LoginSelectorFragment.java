package tseh20.wlunch.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import tseh20.wlunch.R;
import tseh20.wlunch.activities.LoginActivity;
import tseh20.wlunch.authorization.FacebookController;
import tseh20.wlunch.authorization.TwitterController;
import tseh20.wlunch.authorization.VKController;
import tseh20.wlunch.tools.TypefaceManager;

/**
 * Created by ilia on 22.02.16.
 */
public class LoginSelectorFragment extends Fragment {

    @Bind(R.id.title)
    TextView mTitle;

    @Bind(R.id.already_have_account)
    TextView mAlreadyHaveAccount;

    @Bind(R.id.twitter)
    ImageButton mTwitter;

    @Bind(R.id.phone)
    ImageButton mPhone;

    @Bind(R.id.vk)
    ImageButton mVk;

    @Bind(R.id.fb)
    ImageButton mFb;

    TwitterController mTwitterController;
    FacebookController mFacebookController;

    public LoginSelectorFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_selector, container, false);
        ButterKnife.bind(this, rootView);

        mTitle.setTypeface(TypefaceManager.getRegularTypeface(getActivity()));
        mAlreadyHaveAccount.setTypeface(TypefaceManager.getRegularTypeface(getActivity()));

        mPhone.setOnClickListener((view) ->
                ((LoginActivity) getActivity()).loginWithPhone());

        mVk.setOnClickListener((view) ->
            (new VKController(getActivity())).login(getActivity(), (String userId) -> {
                Toast.makeText(getActivity(), "Signed in with id " + userId, Toast.LENGTH_LONG).show();
                ((LoginActivity) getActivity()).startMainActivity();
            }));

        mTwitter.setOnClickListener((view) -> {
            mTwitterController = new TwitterController(getActivity());
            mTwitterController.login(getActivity(), (userName) -> {
                Toast.makeText(getActivity(), "Signed in as " + userName, Toast.LENGTH_LONG).show();
                ((LoginActivity) getActivity()).startMainActivity();
            });
        });

        mFb.setOnClickListener((view) -> {
            mFacebookController = new FacebookController(getActivity());
            mFacebookController.login(getActivity(), (userId) -> {
                Toast.makeText(getActivity(), "Signed in with id " + userId, Toast.LENGTH_LONG).show();
                ((LoginActivity) getActivity()).startMainActivity();
            });
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (mTwitterController != null) {
            mTwitterController.getClient().onActivityResult(requestCode, responseCode, intent);
        }
        if (mFacebookController != null) {
            mFacebookController.getCallbackManager().onActivityResult(requestCode, responseCode, intent);
        }
    }
}
