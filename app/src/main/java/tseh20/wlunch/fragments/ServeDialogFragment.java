package tseh20.wlunch.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import tseh20.wlunch.R;
import tseh20.wlunch.tools.TypefaceManager;

/**
 * Created by ilia on 26.02.16.
 */
public class ServeDialogFragment extends DialogFragment {

    @Bind(R.id.title)
    TextView mTitle;

    @Bind(R.id.number)
    EditText mNumber;

    @Bind(R.id.done)
    TextView mDone;

    Context mContext;
    Callback mCallback;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_serve, null);
        ButterKnife.bind(this, view);

        mTitle.setTypeface(TypefaceManager.getMediumTypeface(mContext));
        mNumber.setTypeface(TypefaceManager.getLightTypeface(mContext));
        mDone.setTypeface(TypefaceManager.getMediumTypeface(mContext));
        mDone.setOnClickListener((View v) -> {
            if (mCallback != null) {
                mCallback.onClick();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    public interface Callback {
        void onClick();
    }
}
