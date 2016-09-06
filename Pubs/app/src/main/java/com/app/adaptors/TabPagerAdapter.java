package com.app.adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.fragments.UpcomingFragment;

/**
 * Created by ram on 28/05/16.
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public TabPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new UpcomingFragment();
           /* case 1:
                return new HistoryFragment();*/
        }
        return null;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 1; //No of Tabs
    }

}
