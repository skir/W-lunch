package tseh20.wlunch.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import tseh20.wlunch.R;
import tseh20.wlunch.activities.DetailActivity;
import tseh20.wlunch.data.DataContract;
import tseh20.wlunch.tools.TypefaceManager;

/**
 * Created by ilia on 24.02.16.
 */
public class CustomDialogFragment extends DialogFragment {


    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.description)
    TextView mDescription;
    @Bind(R.id.distance)
    TextView mDistance;
    @Bind(R.id.type)
    TextView mType;
    @Bind(R.id.price)
    TextView mPrice;
    @Bind(R.id.number)
    TextView mNumber;
    @Bind(R.id.menu)
    TextView mMenu;

    Context mContext;

    int id = 0;

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view  = getActivity().getLayoutInflater().inflate(R.layout.info_window, null);
        ButterKnife.bind(this, view);

        mTitle.setTypeface(TypefaceManager.getMediumTypeface(mContext));
        mDescription.setTypeface(TypefaceManager.getRegularTypeface(mContext));
        mDistance.setTypeface(TypefaceManager.getRegularTypeface(mContext));
        mType.setTypeface(TypefaceManager.getRegularTypeface(mContext));
        mPrice.setTypeface(TypefaceManager.getRegularTypeface(mContext));
        mNumber.setTypeface(TypefaceManager.getRegularTypeface(mContext));
        mMenu.setTypeface(TypefaceManager.getRegularTypeface(mContext));

        Cursor cursor = mContext.getContentResolver().query(DataContract.PostEntry.CONTENT_URI,
                new String[]{DataContract.PostEntry.COLUMN_TITLE, DataContract.PostEntry.COLUMN_BODY},
                DataContract.PostEntry._ID + "=?",
                new String[]{String.valueOf(id)}, null);

        if (cursor != null) {
            cursor.moveToFirst();
            mTitle.setText(cursor.getString(cursor.getColumnIndex(DataContract.PostEntry.COLUMN_TITLE)));
            mDescription.setText(cursor.getString(cursor.getColumnIndex(DataContract.PostEntry.COLUMN_BODY)));
        }

        mMenu.setOnClickListener((View v) -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("postId", id);
            getActivity().startActivity(intent);
        });

        builder.setView(view);
        return builder.create();
    }
}
