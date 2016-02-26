package tseh20.wlunch.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import tseh20.wlunch.R;
import tseh20.wlunch.data.DataContract;
import tseh20.wlunch.tools.TypefaceManager;

/**
 * Created by ilia on 23.02.16.
 */
public class PlacesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Cursor data;
    private static Context mContext;
    private DataSetObserver mDataSetObserver;
    private static ListItemClick mListItemClick;

    private final int TYPE_ITEM = 1;
    private final int TYPE_EMPTY = 0;

    public PlacesAdapter(Context context,Cursor data) {
        this.data = data;
        mContext = context;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (data != null) {
            data.registerDataSetObserver(mDataSetObserver);
        }
    }

    public void setListItemClick(ListItemClick l){
        mListItemClick = l;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_EMPTY:
                return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_empty, parent, false));
            case TYPE_ITEM:
                return new PlacesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }


    @Override
    public int getItemViewType(int position) {
        if(data == null || data.getCount() == 0) {
            return TYPE_EMPTY;
        }
        return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(data != null && data.moveToPosition(position)) {
            ((PlacesViewHolder) holder).title.setText(data.getString(data.getColumnIndex(DataContract.PostEntry.COLUMN_TITLE)));
            ((PlacesViewHolder) holder).description.setText(data.getString(data.getColumnIndex(DataContract.PostEntry.COLUMN_BODY)));
            ((PlacesViewHolder) holder).id = data.getInt(data.getColumnIndex(DataContract.PostEntry._ID));
        }

    }

    @Override
    public int getItemCount() {
        if(data!=null)
            return data.getCount();
        return 0;
    }

    public void swapCursor(Cursor newCursor) {
        if (newCursor == data) {
            return;
        }
        final Cursor oldCursor = data;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        data = newCursor;
        if (data != null) {
            if (mDataSetObserver != null) {
                data.registerDataSetObserver(mDataSetObserver);
            }
            notifyDataSetChanged();
        } else {
            notifyDataSetChanged();
        }
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            notifyDataSetChanged();
        }
    }

    public static class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.description)
        TextView description;
        @Bind(R.id.distance)
        TextView distance;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.number)
        TextView number;
        @Bind(R.id.expand)
        ImageButton expand;
        int id;
        boolean isExpanded = false;

        public PlacesViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            title.setTypeface(TypefaceManager.getMediumTypeface(mContext));
            description.setTypeface(TypefaceManager.getRegularTypeface(mContext));
            distance.setTypeface(TypefaceManager.getRegularTypeface(mContext));
            time.setTypeface(TypefaceManager.getRegularTypeface(mContext));
            price.setTypeface(TypefaceManager.getRegularTypeface(mContext));
            number.setTypeface(TypefaceManager.getRegularTypeface(mContext));

            description.setVisibility(View.GONE);
            number.setVisibility(View.GONE);

            expand.setOnClickListener((View view) -> {
                if (isExpanded) {
                    description.setVisibility(View.GONE);
                    number.setVisibility(View.GONE);
                    expand.setImageResource(R.drawable.ic_down);
                } else {
                    description.setVisibility(View.VISIBLE);
                    number.setVisibility(View.VISIBLE);
                    expand.setImageResource(R.drawable.ic_up);
                }
                isExpanded = !isExpanded;
            });
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListItemClick.listItemClick(id);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
        }
    }

    public interface ListItemClick{
        void listItemClick(int id);
    }
}
