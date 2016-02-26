package tseh20.wlunch.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import tseh20.wlunch.R;
import tseh20.wlunch.activities.LoginActivity;
import tseh20.wlunch.tools.MD5;
import tseh20.wlunch.tools.TypefaceManager;
import tseh20.wlunch.views.CustomEditText;

/**
 * Created by ilia on 24.02.16.
 */
public class LoginForAuthorized extends Fragment {

    @Bind(R.id.phone)
    CustomEditText mPhoneTextView;
    @Bind(R.id.password)
    CustomEditText mPassword;

    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.next)
    TextView mNext;

    String mPhone = "";

    public LoginForAuthorized() {

    }

    public void setPhone(String phone) {
        mPhone = phone;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_for_authorized, container, false);
        ButterKnife.bind(this, rootView);

        mPhoneTextView.setTypeface(TypefaceManager.getRegularTypeface(getActivity()));
        mPassword.setTypeface(TypefaceManager.getRegularTypeface(getActivity()));
        mTitle.setTypeface(TypefaceManager.getRegularTypeface(getActivity()));
        mNext.setTypeface(TypefaceManager.getRegularTypeface(getActivity()));
        mNext.setOnClickListener((View v) -> next());

        mPhoneTextView.setText(mPhone);
        mPhoneTextView.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                mPassword.requestFocus();
                return true;
            }
            return false;
        });

        mPassword.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                next();
                return true;
            }
            return false;
        });

        return rootView;
    }

    private void next() {
        String hash = MD5.calculate(mPassword.getText().toString());
        SharedPreferences preferences = getActivity().getSharedPreferences("preferences", 0);
        String password = preferences.getString("password", "");
        if (hash.equals(password)) {
            ((LoginActivity) getActivity()).startMainActivity();
        } else {
            Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
        }
    }
}
