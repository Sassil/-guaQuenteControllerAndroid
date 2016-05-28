package org.aquacontroller.aguaquentecontroller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPageAdapter extends FragmentPagerAdapter {
    public static final int NUM_PAGES = 2;
    public static final int PAGE_INDICATOR = 0;
    public static final int PAGE_ORDER = 1;

    public interface MainPageNavigator {
	void goToPage(int page);
    }

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
