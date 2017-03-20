package com.cgstate.boxmobile.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cgstate.boxmobile.fragments.ViewDetailFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/20.
 */

public class PhotoViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<ViewDetailFragment> fragments;

    public PhotoViewPagerAdapter(FragmentManager fm, ArrayList<ViewDetailFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
