package tseh20.wlunch.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import tseh20.wlunch.R;
import tseh20.wlunch.adapters.PagerAdapter;
import tseh20.wlunch.tools.TypefaceManager;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;

    PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (mPagerAdapter == null) {
            mPagerAdapter = new PagerAdapter(this, getSupportFragmentManager());
        }

        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        ViewGroup viewGroup = (ViewGroup) mTabLayout.getChildAt(0);
        int tabsCount = viewGroup.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup tab = (ViewGroup) viewGroup.getChildAt(j);
            ((ViewGroup.MarginLayoutParams) tab.getLayoutParams()).setMargins(50*j, 0, 50*(1-j), 0);
            int tabChildsCount = tab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = tab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(TypefaceManager.getRegularTypeface(this));
                    ((TextView) tabViewChild).setAllCaps(false);
                    ((TextView) tabViewChild).setTextSize(15);
                }
            }
        }

    }
}
