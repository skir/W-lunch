package tseh20.wlunch.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import tseh20.wlunch.R;
import tseh20.wlunch.activities.LoginActivity;
import tseh20.wlunch.activities.MainActivity;
import tseh20.wlunch.tools.MD5;
import tseh20.wlunch.tools.TypefaceManager;
import tseh20.wlunch.views.CustomEditText;

/**
 * Created by ilia on 22.02.16.
 */
public class LoginPhoneFragment extends Fragment{

    @Bind(R.id.info)
    CustomEditText mInfo;

    @Bind(R.id.title)
    TextView mTitle;

    @Bind(R.id.next)
    TextView mNext;

    int mStage = 0;
    String phone = "";
    String password = "";

    public LoginPhoneFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_phone, container, false);
        ButterKnife.bind(this, rootView);

        mTitle.setTypeface(TypefaceManager.getRegularTypeface(getActivity()));
        mInfo.setTypeface(TypefaceManager.getRegularTypeface(getActivity()));
        mNext.setTypeface(TypefaceManager.getRegularTypeface(getActivity()));
        mNext.setOnClickListener((View v) -> next());
        mInfo.setInputType(InputType.TYPE_CLASS_PHONE);
        mInfo.setSelection(mInfo.length());
//        mInfo.postDelayed(() -> {
//            mInfo.requestFocus();
//            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//            manager.showSoftInput(mInfo, InputMethodManager.SHOW_IMPLICIT);
//        }, 100);

        mInfo.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                next();
                handled = true;
            }
            return handled;
        });

        return rootView;
    }

    public void onBackPressed() {
        switch (mStage){
            case 1:
                mTitle.setVisibility(View.GONE);
                mInfo.setText("");
                mInfo.setHint(R.string.phone);
                mInfo.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case 2:
                mTitle.setText(getString(R.string.password_description));
                mTitle.setVisibility(View.VISIBLE);
                mInfo.setText("");
                mInfo.setHint(R.string.password);
                mInfo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        mStage--;
    }

    private void next() {
        switch (mStage) {
            case 0:
                phone = mInfo.getText().toString();
                if (!phone.equals("")) {
                    mTitle.setText(getString(R.string.password_description));
                    mTitle.setVisibility(View.VISIBLE);
                    mInfo.setText("");
                    mInfo.setHint(R.string.password);
                    mInfo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mStage++;
                }
                break;
            case 1:
                password = mInfo.getText().toString();
                if (!password.equals("")) {
                    save();
                    mTitle.setText(getString(R.string.invite_code_description));
                    mInfo.setText("");
                    mInfo.setHint(R.string.invite_code);
                    mInfo.setInputType(InputType.TYPE_CLASS_TEXT);
                    mStage++;
                }
                break;
            case 2:
                ((LoginActivity) getActivity()).startMainActivity();
        }
    }

    private void save() {
        SharedPreferences preferences = getActivity().getSharedPreferences("preferences", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("login_mode", "phone");
        editor.putString("userId", phone);
        editor.putString("password", MD5.calculate(password));
        editor.apply();
    }
}
