package com.alim.freefire.gamershub.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.alim.freefire.gamershub.Fragment.HubFragment;
import com.alim.freefire.gamershub.Fragment.LiveFragment;
import com.alim.freefire.gamershub.Fragment.SettingsFragment;
import com.alim.freefire.gamershub.Fragment.TrendingFragment;

public class PagerAdapter {

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HubFragment();
                case 1:
                    return new TrendingFragment();
                case 2:
                    return new LiveFragment();
                case 3:
                    return new SettingsFragment();
            }
            return new HubFragment();
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
