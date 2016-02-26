package tseh20.wlunch.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import tseh20.wlunch.R;
import tseh20.wlunch.activities.DetailActivity;
import tseh20.wlunch.adapters.PlacesAdapter;
import tseh20.wlunch.data.DataContract;
import tseh20.wlunch.data.WebService;
import tseh20.wlunch.tools.DividerItemDecoration;

/**
 * Created by ilia on 22.02.16.
 */
public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    PlacesAdapter mPlacesAdapter;

    int attemptsToLoad = 0;
    final int PLACES_LOADER = 1;

    public ListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPlacesAdapter = new PlacesAdapter(getContext(), null);
        mPlacesAdapter.setListItemClick((int id) -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("postId", id);
            getActivity().startActivity(intent);
        });

        mRecyclerView.setAdapter(mPlacesAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        getActivity().getSupportLoaderManager().initLoader(PLACES_LOADER, null, this);

        mSwipeRefreshLayout.setOnRefreshListener(() -> refresh());

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        return new CursorLoader(getContext(),
                DataContract.PostEntry.CONTENT_URI,
                new String[]{DataContract.PostEntry.COLUMN_TITLE, DataContract.PostEntry.COLUMN_BODY, DataContract.PostEntry._ID},
                null, null, null);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mPlacesAdapter.swapCursor(null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.e("finished", String.valueOf(data.getCount()));
        if(data.getCount() == 0) {
            attemptsToLoad++;
            if(attemptsToLoad < 2) {
                refresh();
            }
        }

        mPlacesAdapter.swapCursor(data);
    }

    private void refresh() {
        WebService.getWebservice(getContext()).getPosts();
    }
}
