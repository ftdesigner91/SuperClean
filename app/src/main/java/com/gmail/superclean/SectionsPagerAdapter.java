package com.gmail.superclean;

import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public SectionsPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;

            case 1:
                JobsFragment jobsFragment = new JobsFragment();
                return jobsFragment;

            case 2:
                ChatFragment ChatFragment = new ChatFragment();
                return ChatFragment;




            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    public  CharSequence getPageTitle(int position)
    {
        switch(position)
        {
            case 0:
                return "Home";
            case 1:
                return  "Jobs";
            default:
                return null;
        }

    }
}
