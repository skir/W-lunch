package tseh20.wlunch.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import tseh20.wlunch.R;
import tseh20.wlunch.fragments.ListFragment;
import tseh20.wlunch.fragments.MapFragment;

/**
 * Created by ilia on 23.02.16.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    ListFragment listFragment;
    MapFragment mapFragment;
    Context mContext;

    public PagerAdapter(Context context, FragmentManager manager) {
        super(manager);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            listFragment = new ListFragment();
            return listFragment;
        }
        mapFragment = new MapFragment();
        return mapFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.places);
            case 1:
                return mContext.getString(R.string.discover);
            default:
                return "";
        }
    }
}