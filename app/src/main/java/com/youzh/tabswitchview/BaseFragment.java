package com.youzh.tabswitchview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by youzehong on 16/7/10.
 */
public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = onLayoutId();
        if (layoutId > 0) {
            View mContentView = inflater.inflate(layoutId, null, false);
            onLayoutCreate();
            return  mContentView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected Context mContext;

    protected abstract int onLayoutId();

    protected abstract void onLayoutCreate();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

}
