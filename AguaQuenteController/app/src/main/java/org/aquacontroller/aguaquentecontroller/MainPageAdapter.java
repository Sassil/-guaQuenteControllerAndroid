package org.aquacontroller.aguaquentecontroller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPageAdapter extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 2;
    private static final int PAGE_INDICATOR = 0;
    private static final int PAGE_ORDER = 1;

    public MainPageAdapter(FragmentManager fm) {
	super(fm);
    }

    @Override
    public int getCount() {
	return NUM_PAGES;
    }

    @Override
    public Fragment getItem(int position) {
	switch (position) {
	    case PAGE_INDICATOR:
		return new IndicatorsFragment();
	    case PAGE_ORDER:
		return new OrderFragment();
	}
	return null;
    }

}
