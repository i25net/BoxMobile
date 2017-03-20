package com.cgstate.boxmobile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/3/20.
 */

public abstract class BaseFragment extends Fragment {
    public Context mContext;
    private View view = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    // 一个View只能被 添加布局里面ViewPager一次
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = createView(inflater, container, savedInstanceState);
        } else {
            // has parent
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;

    }


    protected boolean isVisibleToUser;
    protected boolean isViewInitiated;
    protected boolean isDataInitiated;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        isViewInitiated = true;
        prepareFetchData();
    }

    public boolean prepareFetchData() {
        return prepareFetchData(true);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }


    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void fetchData();

}
