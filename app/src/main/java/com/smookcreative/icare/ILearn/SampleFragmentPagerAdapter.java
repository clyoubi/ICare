package com.smookcreative.icare.ILearn;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"A Lire", "Infos", "Vidéos"};

    public SampleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return NewsFragment.newInstance(0);
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return DepecheFragment.newInstance(1);
            case 2: // Fragment # 1 - This will show SecondFragment
                return VideosFragment.newInstance(2);
            default:
                return null;
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];


    }

}