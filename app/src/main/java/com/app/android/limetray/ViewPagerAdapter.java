package com.app.android.limetray;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.android.limetray.fragments.FragmentTweetGraph;
import com.app.android.limetray.fragments.FragmentTweets;


/**
 * Created by blackadmin on 18/9/14.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    // Tab Titles
    private String tabTitles[] = new String[]{"TWEETS", "TWEET PLOT"};

    // Declare the number of ViewPager pages
    private final int PAGE_COUNT = 2;

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FragmentTweets.newInstance();
            case 1:
                return FragmentTweetGraph.newInstance();

        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
