package tseh20.wlunch.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import tseh20.wlunch.R;
import tseh20.wlunch.data.DataContract;
import tseh20.wlunch.tools.TypefaceManager;

/**
 * Created by ilia on 25.02.16.
 */
public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Cursor data;
    private static Context mContext;
    private DataSetObserver mDataSetObserver;

    private final int TYPE_HEADER = 2;
    private final int TYPE_ITEM = 1;
    private final int TYPE_EMPTY = 0;

    public MenuAdapter(Context context,Cursor data) {
        this.data = data;
        mContext = context;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (data != null) {
            data.registerDataSetObserver(mDataSetObserver);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_HEADER:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_menu_header, parent, false));
            case TYPE_EMPTY:
                return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_empty, parent, false));
            case TYPE_ITEM:
                return new MenuViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_menu, parent, false));
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }


    @Override
    public int getItemViewType(int position) {
        if(data == null || data.getCount() == 0) {
            return TYPE_EMPTY;
        } else if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(data != null && data.moveToPosition(position - 1)) {
            ((MenuViewHolder) holder).item.setText(data.getString(data.getColumnIndex(DataContract.CommentEntry.COLUMN_NAME)));
        }

    }

    @Override
    public int getItemCount() {
        if(data!=null)
            return data.getCount() + 1;
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

    public static class MenuViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item)
        TextView item;

        public MenuViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            item.setTypeface(TypefaceManager.getMediumTypeface(mContext));
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.menu)
        TextView menu;
        public HeaderViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            time.setTypeface(TypefaceManager.getRegularTypeface(mContext));
            price.setTypeface(TypefaceManager.getRegularTypeface(mContext));
            menu.setTypeface(TypefaceManager.getRegularTypeface(mContext));
        }
    }
}
