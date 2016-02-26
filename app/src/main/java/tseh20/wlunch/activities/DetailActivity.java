package tseh20.wlunch.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import tseh20.wlunch.R;
import tseh20.wlunch.adapters.MenuAdapter;
import tseh20.wlunch.data.DataContract;
import tseh20.wlunch.data.WebService;
import tseh20.wlunch.fragments.DoneDialogFragment;
import tseh20.wlunch.fragments.ServeDialogFragment;
import tseh20.wlunch.tools.DividerItemDecoration;
import tseh20.wlunch.tools.TypefaceManager;

/**
 * Created by ilia on 24.02.16.
 */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.serve)
    TextView mServe;

    @Bind(R.id.image)
    ImageView mImage;

    MenuAdapter mMenuAdapter;
    int attemptsToLoad = 0;
    final int MENU_LOADER = 2;

    int mPostId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        Intent intent = getIntent();
        if (intent != null) {
            mPostId = intent.getIntExtra("postId", 0);
        }

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMenuAdapter = new MenuAdapter(this, null);

        mRecyclerView.setAdapter(mMenuAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mImage.getLayoutParams().height = 9 * metrics.widthPixels / 16;
        //use picassa for loading images from web

        mServe.setTypeface(TypefaceManager.getMediumTypeface(this));
        mServe.setOnClickListener((View v) -> {
            ServeDialogFragment dialog = new ServeDialogFragment();
            dialog.setCallback(() -> {
                dialog.dismiss();
                DoneDialogFragment newDialog = new DoneDialogFragment();
                newDialog.setCallback(() -> newDialog.dismiss());
                newDialog.show(getSupportFragmentManager(), "done");
            });
            dialog.show(getSupportFragmentManager(), "serve");
        });

        getSupportLoaderManager().initLoader(MENU_LOADER, null, this);

        mSwipeRefreshLayout.setOnRefreshListener(() -> refresh());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        return new CursorLoader(this,
                DataContract.CommentEntry.buildUriByPost(mPostId),
                new String[]{DataContract.CommentEntry.COLUMN_NAME, DataContract.CommentEntry.COLUMN_BODY},
                null, null, null);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mMenuAdapter.swapCursor(null);
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

        mMenuAdapter.swapCursor(data);
    }

    private void refresh() {
        WebService.getWebservice(this).getComments(mPostId);
    }
}
